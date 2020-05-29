package com.cycs.poskp.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cycs.common.redis.cache.service.RedisService;
import com.cycs.poskp.Constants;
import com.cycs.poskp.beans.EnterpriseBean;
import com.cycs.poskp.beans.InvoiceBean;
import com.cycs.poskp.beans.InvoiceDetailBean;
import com.cycs.poskp.beans.InvoiceExtendBean;
import com.cycs.poskp.beans.JsonResult;
import com.cycs.poskp.beans.PosInformationBean;
import com.cycs.poskp.beans.PreferenceInvoiceItemBean;
import com.cycs.poskp.beans.RDSfzrdxxbVO;
import com.cycs.poskp.dao.EnterpriseExtendMapper;
import com.cycs.poskp.dao.InvoiceExtendMapper;
import com.cycs.poskp.dto.EnterpriseDto;
import com.cycs.poskp.dto.InvoiceInfoDto;
import com.cycs.poskp.dto.InvoiceQueryDto;
import com.cycs.poskp.dto.JsonResponse;
import com.cycs.poskp.entity.EnterpriseExtend;
import com.cycs.poskp.entity.InvoiceExtend;
import com.cycs.poskp.enums.ResponseCodeEnum;
import com.cycs.poskp.service.IInvoiceService;
import com.cycs.poskp.util.login.LoginUtil;
import com.cycs.poskp.util.okhttp.OkHttpUtil;
import com.cycs.poskp.util.utils.UuidUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 发票开具接口实现类
 * 
 * @author mameisong
 */
@Service("invoiceServiceImpl")
@Slf4j
public class InvoiceServiceImpl implements IInvoiceService {

	@Autowired
	private EnterpriseExtendMapper enterpriseExtendDao;

	@Autowired
	private InvoiceExtendMapper invoiceExtendMapper;

	@Autowired
	private RedisService rs;

	@Autowired
	private OkHttpUtil okHttp;

	@Autowired
	private LoginUtil loginUtil;

	@Value("${http.url.dkurl}")
	private String dkurl;

	@Value("${http.url.query-invoice}")
	private String queryInvoiceUrl;

	@Value("${area-code}")
	private String areaCode;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cycs.poskp.service.IInvoiceService#invoice(com.cycs.poskp.dto.InvoiceDto)
	 */
	@Override
	@Transactional
	public JsonResponse<?> invoice(InvoiceInfoDto invoiceDto) {
		InvoiceBean serverParameterJson = invoiceDto.getServerParameterJson();

		try {
			addInvoiceExtend(serverParameterJson);

			// 获取税费种认定信息
			RDSfzrdxxbVO rdxx = rs.hashOps().get(Constants.SFZ_KEY_POSKP_PRE,
					serverParameterJson.getEnterprise().getZspmDm());
			if (null == rdxx) {
				return new JsonResponse<>(ResponseCodeEnum.RESP_ERROR);
			}
			// 获取企业信息
			EnterpriseExtend ent = enterpriseExtendDao
					.selectByPrimaryKey(new EnterpriseExtend(serverParameterJson.getPosId()));
			if (!checkArg(ent)) {
				return new JsonResponse<>(ResponseCodeEnum.INCOMPLETE_INFORMATION);
			}
			// token
			String token = rs.valueOps().get(Constants.CACHE_KEY_POSKP_PRE);
			// 开票时需要的fakeId
			String tmpToken = rs.valueOps()
					.get(String.format(Constants.TEMPTOKEN_KEY_POSKP_PRE, serverParameterJson.getPosId()));
			String zspmType = zspmType(serverParameterJson.getList());
			if ("error".equals(zspmType)) {
				return new JsonResponse<>(ResponseCodeEnum.ZSPMTYPE_NOT_AGREEMENT);
			}
			kpParamAss(serverParameterJson, rdxx, ent, tmpToken, zspmType);
			String param = JSON.toJSONString(serverParameterJson);
			Map<String, String> map = new HashMap<String, String>();
			map.put("data", param);
			map.put("token", token);
			String kpUrl = String.format(dkurl, "invoice\\save");
			String response = okHttp.postForm(kpUrl, map);
			if (StringUtils.isBlank(response)) {
				return new JsonResponse<>(ResponseCodeEnum.RESP_ERROR);
			}
			JsonResult result = JSON.parseObject(response, JsonResult.class);
			if ("419".equals(result.getFlag())) {
				String str = loginUtil.login();
				if ("0".equals(str)) {
					response = okHttp.postForm(kpUrl, map);
					result = JSON.parseObject(response, JsonResult.class);
					if ("0".equals(result.getFlag())) {
						rs.valueOps().del(String.format(Constants.TEMPTOKEN_KEY_POSKP_PRE, serverParameterJson.getPosId()));
						Map<String, String> mapQuery = new HashMap<String, String>();
						mapQuery.put("areaCode", areaCode);
						mapQuery.put("id", String.valueOf(result.getObject()));
						log.info("通知抓票参数：{}",JSON.toJSONString(mapQuery));
						okHttp.postForm(queryInvoiceUrl, mapQuery);
						updateInvoiceExtend(serverParameterJson.getSerialNumber(), String.valueOf(result.getObject()));
					}

				}
			} else if ("0".equals(result.getFlag())) {
				rs.valueOps().del(String.format(Constants.TEMPTOKEN_KEY_POSKP_PRE, serverParameterJson.getPosId()));
				Map<String, String> mapQuery = new HashMap<String, String>();
				mapQuery.put("areaCode", areaCode);
				mapQuery.put("id", String.valueOf(result.getObject()));
				log.info("通知抓票参数：{}",JSON.toJSONString(mapQuery));
				okHttp.postForm(queryInvoiceUrl, mapQuery);
				updateInvoiceExtend(serverParameterJson.getSerialNumber(), String.valueOf(result.getObject()));
			} else {
				return new JsonResponse<>(ResponseCodeEnum.RESP_ERROR);
			}
		} catch (Exception e) {
			if (null != e.getCause() && e.getCause().getMessage().contains("ORA-00001")) {
				return new JsonResponse<>(ResponseCodeEnum.REPEATED_SUBMIT);
			}
			log.error("添加发票扩展信息出现异常：", e);
		}
		return new JsonResponse<>();
	}

	/**
	 * 征收品目类型处理 类型 1货物劳务(spbm12开头) 2服务(spbm345开头) 3其他
	 * 
	 * @author mameisong
	 * @param list 开票项列表
	 * @return
	 */
	private String zspmType(List<InvoiceDetailBean> list) {
		String spbmCode = "";
		for (InvoiceDetailBean detail : list) {
			if (!StringUtils.isBlank(spbmCode) && !spbmCode.equals(detail.getSpbmCode())) {
				return "error";
			}
			spbmCode = detail.getSpbmCode();
			int sc = Integer.valueOf(spbmCode.substring(0, 1));
			if (sc < 6 && sc > 2) {
				return "2";
			} else if (sc < 3) {
				return "1";
			}
		}
		return "3";

	}

	/**
	 * 开票参数组装
	 * 
	 * @author mameisong
	 * @param serverParameterJson
	 * @param rdxx                认定信息
	 * @param ent                 销方信息
	 * @param tmpToken            fakeId
	 * @param zspmType            征收品目类型
	 */
	private void kpParamAss(InvoiceBean serverParameterJson, RDSfzrdxxbVO rdxx, EnterpriseExtend ent, String tmpToken,
			String zspmType) {
		serverParameterJson.setKpr(ent.getKpr());
		serverParameterJson.setFhr(ent.getFhr());
		serverParameterJson.setSkr(ent.getSkr());
		serverParameterJson.setUserId(ent.getUserId());
		serverParameterJson.setType(Constants.TYPE_ONE);
		serverParameterJson.setFakeId(tmpToken);
		serverParameterJson.setEnterpriseId(ent.getEnterpriseId());
		serverParameterJson.getEnterprise().setAddress(ent.getXsfDz());
		serverParameterJson.getEnterprise().setZspmType(zspmType);
		serverParameterJson.getEnterprise().setContacts(ent.getXsfDh());
		serverParameterJson.getEnterprise().setBankAccount(ent.getXsfYhzh());
		serverParameterJson.getEnterprise().setZsl(rdxx.getZsl());
		serverParameterJson.getEnterprise().setAmountUnit(rdxx.getNsqxDm());
		serverParameterJson.getEnterprise().setHyDm(rdxx.getHyDm());
		serverParameterJson.getEnterprise().setZspmMc(rdxx.getZspmMc());

		BigDecimal hjje = new BigDecimal(0);
		BigDecimal hjse = new BigDecimal(0);
		BigDecimal jshj = new BigDecimal(0);

		for (InvoiceDetailBean detail : serverParameterJson.getList()) {
			detail.setId(detail.getSpbmCode());
			detail.setSpbmCode(null);
			// 含税销售额
			jshj = jshj.add(detail.getHsxse());
			// 不含税金额
			BigDecimal bhsje = detail.getHsxse().divide(new BigDecimal(rdxx.getZsl()).add(new BigDecimal("1")), 8,
					BigDecimal.ROUND_HALF_UP);
			detail.setSl(new BigDecimal(rdxx.getZsl()));
			String strDj = bhsje.divide(detail.getXmsl(), 6, BigDecimal.ROUND_HALF_UP).stripTrailingZeros()
					.toPlainString();
			BigDecimal dj = new BigDecimal(strDj);
			detail.setXmdj(dj);
			// 税额
			bhsje = bhsje.setScale(2, BigDecimal.ROUND_HALF_DOWN);
			BigDecimal se = detail.getHsxse().subtract(bhsje);
			hjse = hjse.add(se);
			detail.setSe(se);
			detail.setXmje(bhsje);
			hjje = hjje.add(bhsje);
		}
		serverParameterJson.setHjje(hjje);
		serverParameterJson.setHjse(hjse);
		serverParameterJson.setJshj(jshj);

		serverParameterJson.setPosId(null);
		serverParameterJson.setBz(String.format("代开企业税号:%s;代开企业名称:%s",ent.getTaxpayerNum(),ent.getEnterpriseName()));
	}

	/**
	 * 添加发票信息
	 * 
	 * @author mameisong
	 * @param serverParameterJson
	 * @param result
	 * @return
	 */
	private JsonResponse<?> addInvoiceExtend(InvoiceBean serverParameterJson) {
		try {
			InvoiceExtend invoiceExtend = new InvoiceExtend();
			Date date = new Date();
			invoiceExtend.setCreateTime(date);
			invoiceExtend.setModifyTime(date);
			invoiceExtend.setPosId(serverParameterJson.getPosId());
			invoiceExtend.setId(UuidUtil.uuid());
			invoiceExtend.setStatus(0);
			invoiceExtend.setSerialNumber(serverParameterJson.getSerialNumber());
			invoiceExtendMapper.insert(invoiceExtend);
		} catch (Exception e) {
			if (null != e.getCause() && e.getCause().getMessage().contains("ORA-00001")) {
				return new JsonResponse<>(ResponseCodeEnum.REPEATED_SUBMIT);
			}
			log.error("添加发票扩展信息出现异常：", e);
		}
		return new JsonResponse<>();
	}

	/**
	 * 修改发票信息
	 * 
	 * @author mameisong
	 * @param serialNumber
	 * @param invoiceId
	 */
	private void updateInvoiceExtend(String serialNumber, String invoiceId) {
		try {
			InvoiceExtend invoiceExtend = new InvoiceExtend();
			Date date = new Date();
			invoiceExtend.setModifyTime(date);
			invoiceExtend.setSerialNumber(serialNumber);
			invoiceExtend.setInvoiceId(invoiceId);
			invoiceExtendMapper.updateByPrimaryKeySelective(invoiceExtend);
		} catch (Exception e) {
			if (null != e.getCause() && e.getCause().getMessage().contains("ORA-00001")) {
				// 违反唯一约束，不做处理入库成功的就让他成功
				log.error("发票数据入库失败,违反了唯一约束====不处理========》");
			} else {
				log.error("发票数据入库失败存入异常信息{}", e);
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * 通知服务平台抓取发票
	 * 
	 * @author mameisong
	 * @param response
	 */
	private void queryInvoice2(String response) {
		JsonResult result = null;
		if (!StringUtils.isBlank(response)) {
			result = JSON.parseObject(response, JsonResult.class);
			Map<String, String> mapQuery = new HashMap<String, String>();
			mapQuery.put("areaCode", areaCode);
			mapQuery.put("id", String.valueOf(result.getObject()));
			okHttp.getQueryString(queryInvoiceUrl, mapQuery);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cycs.poskp.service.IInvoiceService#applyForInvoice(com.cycs.poskp.dto.
	 * EnterpriseDto)
	 */
	@Override
	public JsonResponse<?> applyForInvoice(EnterpriseDto enterpriseDto) {
		EnterpriseBean bean = enterpriseDto.getServerParameterJson();
		EnterpriseExtend ent = new EnterpriseExtend(bean.getPosId());
		ent = enterpriseExtendDao.selectByPrimaryKey(ent);
		if (Objects.isNull(ent)) {
			return new JsonResponse<>(ResponseCodeEnum.DATA_NOT_EXISTENCE);
		}
		EnterpriseBean paramBean = new EnterpriseBean();
		paramBean.setEnterpriseId(ent.getEnterpriseId());
		paramBean.setId(ent.getUserId());
		String token = rs.valueOps().get(Constants.CACHE_KEY_POSKP_PRE);
		if (StringUtils.isBlank(token)) {// token没有值调用登录接口
			String res = loginUtil.login();
			if ("1".equals(res)) {
				return new JsonResponse<>(ResponseCodeEnum.RESP_ERROR);
			}
		}

		// 查询企业和税费种信息
		String param = JSON.toJSONString(paramBean);
		Map<String, String> map = new HashMap<String, String>() {// 用一句话描述这个变量表示什么
			private static final long serialVersionUID = 1L;
			{
				put("data", param);
				put("token", token);
			}
		};
		String applyUrl = String.format(dkurl, "invoice\\applyForInvoice");
		String response = okHttp.postForm(applyUrl, map);
		JSONObject obj = JSON.parseObject(response);
		PosInformationBean posInformationBean = null;
		if ("419".equals(obj.getString("flag"))) {// 如果flag是419 调用登录接口获取token
			String res = loginUtil.login();
			if ("1".equals(res)) {
				return new JsonResponse<>(ResponseCodeEnum.RESP_ERROR);
			} else {
				response = okHttp.postForm(applyUrl, map);
				obj = JSON.parseObject(response);
				if ("0".equals(obj.getString("flag"))) {
					posInformationBean = applyForInvoiceRespDea(token, map, obj, bean.getPosId());
					return new JsonResponse<PosInformationBean>(posInformationBean);
				} else {
					return new JsonResponse<>(ResponseCodeEnum.RESP_ERROR);
				}
			}

		} else {
			if ("0".equals(obj.getString("flag"))) {
				posInformationBean = applyForInvoiceRespDea(token, map, obj, bean.getPosId());
				return new JsonResponse<PosInformationBean>(posInformationBean);
			} else {
				return new JsonResponse<>(ResponseCodeEnum.RESP_ERROR);
			}
		}
	}

	/**
	 * 开票申请请求后数据处理
	 * 
	 * @author mameisong
	 * @param token
	 * @param map
	 * @param obj
	 * @param posId
	 * @return
	 */
	private PosInformationBean applyForInvoiceRespDea(String token, Map<String, String> map, JSONObject obj,
			String posId) {
		EnterpriseBean paramBean;
		String param;
		String applyUrl;
		String response;
		EnterpriseBean enterpriseResult = JSON.parseObject(obj.getString("object"), EnterpriseBean.class);
		rs.valueOps().set(String.format(Constants.TEMPTOKEN_KEY_POSKP_PRE, posId), enterpriseResult.getTmpToken());
		// 查询常用开票项
		paramBean = new EnterpriseBean();
		paramBean.setEnterpriseId(enterpriseResult.getId());
		param = JSON.toJSONString(paramBean);
		Map<String, String> map1 = new HashMap<String, String>() {// 用一句话描述这个变量表示什么
			private static final long serialVersionUID = 1L;
			{
				put("data", param);
				put("token", token);
			}
		};

		applyUrl = String.format(dkurl, "/invoice/findOwnPreferenceInvoiceItem");
		response = okHttp.postForm(applyUrl, map1);
		JsonResult result = JSON.parseObject(response, JsonResult.class);
		List<PreferenceInvoiceItemBean> list = JSON.parseArray(result.getObject().toString(),
				PreferenceInvoiceItemBean.class);

		// 税费种
		PosInformationBean posInformationBean = new PosInformationBean();
		List<RDSfzrdxxbVO> rdSfzrdxxbVOList = enterpriseResult.getRdSfzrdxxbVOList();
		enterpriseResult.setRdSfzrdxxbVOList(null);
		List<RDSfzrdxxbVO> rdSfzrdxxbList = new ArrayList<RDSfzrdxxbVO>();
		for (RDSfzrdxxbVO rDSfzrdxxbVO : rdSfzrdxxbVOList) {
			RDSfzrdxxbVO vo = new RDSfzrdxxbVO();
			vo.setZspmDm(rDSfzrdxxbVO.getZspmDm());
			vo.setZspmMc(rDSfzrdxxbVO.getZspmMc());
			vo.setZsxmDm(rDSfzrdxxbVO.getZsxmDm());
			vo.setZspmType(rDSfzrdxxbVO.getZspmType());
			rdSfzrdxxbList.add(vo);
		}

		// 组装返回参数
		posInformationBean.setRdSfzrdxxbVOList(rdSfzrdxxbList);
		posInformationBean.setInvoiceItem(list);

		// 存放税费种缓存
		if (rs.hasKey(Constants.SFZ_KEY_POSKP_PRE)) {
			rs.del(Constants.SFZ_KEY_POSKP_PRE);
		}
		for (RDSfzrdxxbVO rDSfzrdxxbVO : rdSfzrdxxbVOList) {
			rs.hashOps().set(Constants.SFZ_KEY_POSKP_PRE, rDSfzrdxxbVO.getZspmDm(), rDSfzrdxxbVO);
		}
		return posInformationBean;
	}

	private boolean checkArg(EnterpriseExtend ent) {
		if (null == ent) {
			return false;
		}
		if (null == ent.getFhr() || null == ent.getKpr() || null == ent.getSkr() || null == ent.getXsfDz()
				|| null == ent.getXsfDh()) {
			return false;
		}
		return true;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cycs.poskp.service.IInvoiceService#getList(com.cycs.poskp.dto.Page)
	 */
	@Override
	public JsonResponse<List<InvoiceExtend>> getList(InvoiceQueryDto invoiceQueryDto) {
		InvoiceExtendBean invoiceExtendBean = invoiceQueryDto.getServerParameterJson();
		if (ObjectUtils.isEmpty(invoiceExtendBean) || null == invoiceExtendBean.getSerialNumbers()) {
			return new JsonResponse<>(ResponseCodeEnum.PARAM_INVALID, "查询参数不能为空");
		}
		List<InvoiceExtend> list = invoiceExtendMapper.getList(invoiceExtendBean);
		return new JsonResponse<List<InvoiceExtend>>(list);
	}
}
