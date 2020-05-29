package com.cycs.poskp.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cycs.common.redis.cache.service.RedisService;
import com.cycs.poskp.Constants;
import com.cycs.poskp.beans.InvoiceBean;
import com.cycs.poskp.beans.InvoiceExtendBean;
import com.cycs.poskp.beans.JsonResult;
import com.cycs.poskp.dao.InvoiceExtendMapper;
import com.cycs.poskp.entity.InvoiceExtend;
import com.cycs.poskp.task.pool.GrabInvoiceThreadPool;
import com.cycs.poskp.util.login.LoginUtil;
import com.cycs.poskp.util.okhttp.OkHttpUtil;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GrabInvoiceScheduling {

	@Autowired
	private InvoiceExtendMapper invoiceExtendDao;
	@Autowired
	private GrabInvoiceThreadPool threadPool;
	
	@Autowired
	private OkHttpUtil okHttp;

	@Autowired
	private LoginUtil loginUtil;
	
	@Value("${http.url.query-invoice}")
	private String queryInvoiceUrl;

	@Value("${area-code}")
	private String areaCode;

	@Autowired
	private RedisService rs;

	@Value("${http.url.dkurl}")
	private String dkurl;

	@Value("${grab.thread.sleep:1000}")
	private Long grabThreadSleep;
	
	@Value("${http.url.download}")
	private String downPdfPrefix;
	
	@Scheduled(cron = "${task.cron.grab}")
	public void grabInvoice() {

		List<InvoiceExtendBean> inProcess = invoiceExtendDao.selectInProcessing(new InvoiceExtend());
		/*
		 * List<List<InvoiceExtendBean>> inProcessGroup = new
		 * ArrayList<List<InvoiceExtendBean>>(); inProcess.stream()
		 * .collect(Collectors.groupingBy(InvoiceExtendBean::getPosId,Collectors.toList(
		 * ))) .forEach((posId,fooListByName)->{ inProcessGroup.add(fooListByName); });
		 */
		log.debug("需要处理的发票条数{}",inProcess.size());
		inProcess.forEach(item -> {
			String token = rs.valueOps().get(Constants.CACHE_KEY_POSKP_PRE);
			threadPool.execute(() -> {
				InvoiceExtendBean bean = new InvoiceExtendBean(item.getUserId(), item.getEnterpriseId(),
						item.getInvoiceId(), 1, 1, 1, 1);

				Map<String, String> map = new HashMap<String, String>();
				map.put("data", JSON.toJSONString(bean));
				map.put("token", token);
				String kpUrl = String.format(dkurl, "/invoice/findOwnInvoice");
				String response = okHttp.postForm(kpUrl, map);
				JsonResult result = JSON.parseObject(response, JsonResult.class);
				if (result.getFlag().equals("0")) {
					dealResp(item, result);
				} else if ("419".equals(result.getFlag())) {
					String rs = loginUtil.login();
					if ("0".equals(rs)) {
						response = okHttp.postForm(kpUrl, map);
						result = JSON.parseObject(response, JsonResult.class);
						if (result.getFlag().equals("0")) {
							dealResp(item, result);
						}
					}
				}
			});
		});
	}
	private void dealResp(InvoiceExtendBean item, JsonResult result) {
		List<InvoiceBean> inv = JSON.parseArray(result.getObject().toString(), InvoiceBean.class);
		InvoiceBean ib = inv.get(0);
		String pdfUrl = createPdfUrl(ib.getEnterpriseId(), ib.getFpDm(), ib.getFpHm(), "pdf", "");
		InvoiceExtend updateBean = new InvoiceExtend(item.getId(), Integer.valueOf(ib.getStatus()), pdfUrl,
				ib.getReturnmsg());
		invoiceExtendDao.updateByPrimaryKeySelective(updateBean);
		Map<String, String> mapQuery = new HashMap<String, String>();
		mapQuery.put("areaCode", areaCode);
		mapQuery.put("id", item.getInvoiceId());
		log.info("通知抓票参数：{}",JSON.toJSONString(mapQuery));
		okHttp.postForm(queryInvoiceUrl, mapQuery);
	}

	/**
	 * 发票png/pdf地址拼接
	 * 
	 * @author mameisong
	 * @param enterpriseId
	 * @param fpDm
	 * @param fpHm
	 * @param type
	 * @param name
	 * @return
	 */
	private String createPdfUrl(String enterpriseId, String fpDm, String fpHm, String type, String name) {
		try {
			// https://ip:port/userName/fakeClientId/fpdmhlwjlwsjs/sewwdm/fphmqswfpdmhlw/lwsjsfphmhsw/lx/sewsjs
			// ip=域名或ip地址
			// port=端口
			// userName=企业用户名
			// fakeClientId=伪客户端id 生成规则：0-10的随机数+横线（“-”）+18位或19位随机数 例如：1-843742971642785792
			// fpdmhlwjlwsjs=发票代码后六位+六位随机数 生成规则： 截取发票代码后六位+六位随机数字
			// sewwdm=十二位随机数
			// fphmqswfpdmhlw=发票号码前四位+发票代码前六位 生成规则： 截取发票号码前四位+截取发票代码前六位
			// Lwsjsfphmhsw=发票号码后四位 生成规则： 截取发票号码后四位
			// Lx=类型 生成规则： 缩略图：png 文件：pdf
			// sewsjs=十二位随机数

			StringBuffer sb = new StringBuffer();
			String userName = enterpriseId;
			String fakeClientId = randomNumStr(1) + "-" + randomNumStr(18);
			String fpdmhlwjlwsjs = fpDm.substring(6) + randomNumStr(6);
			String sewwdm = randomNumStr(12);
			String fphmqswfpdmhlw = fpHm.substring(0, 4) + fpDm.substring(0, 6);
			String lwsjsfphmhsw = fpHm.substring(4) + name;
			String sewsjs = randomNumStr(12);
			sb.append(downPdfPrefix).append("/").append(userName).append("/").append(fakeClientId).append("/")
					.append(fpdmhlwjlwsjs).append("/").append(sewwdm).append("/").append(fphmqswfpdmhlw).append("/")
					.append(lwsjsfphmhsw).append("/").append(type).append("/").append(sewsjs);
			return sb.toString();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 生成指定长度的随机数
	 *
	 * @param length
	 * @return
	 * @throws Exception
	 */
	private String randomNumStr(int length) throws Exception {
		Random r = new Random();
		long value = r.nextLong() + r.nextInt(999999999) + r.nextLong();
		StringBuffer sb = new StringBuffer();
		sb.append(Math.abs(r.nextInt(100000)));
		String valueStr = String.valueOf(Math.abs(value));
		if (valueStr.length() < length) {
			for (int i = 0; i <= length - valueStr.length(); i++) {
				sb.append(Math.abs(r.nextInt(10)));
			}
		}
		sb.append(valueStr);
		return sb.toString().substring(0, length);
	}
}
