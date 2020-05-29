package com.cycs.poskp.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cycs.poskp.dao.EnterpriseExtendMapper;
import com.cycs.poskp.dto.JsonResponse;
import com.cycs.poskp.entity.EnterpriseExtend;
import com.cycs.poskp.enums.ResponseCodeEnum;
import com.cycs.poskp.service.IEnterpriseService;
import com.cycs.poskp.util.utils.UuidUtil;

/**
 * 	企业信息接口实现类
 * @author wangrongxin
 */
@Service("enterpriseServiceImpl")
public class EnterpriseServiceImpl implements IEnterpriseService {
	@Autowired
	private EnterpriseExtendMapper enterpriseExtendMapper;

	@Override
	public JsonResponse<?> add(EnterpriseExtend enterpriseExtend) {
		EnterpriseExtend extend = new EnterpriseExtend();
		extend.setEnterpriseId(enterpriseExtend.getEnterpriseId());
		extend = enterpriseExtendMapper.selectByPrimaryKey(enterpriseExtend);
		if(null != extend) {
			return new JsonResponse<>(ResponseCodeEnum.DATA_EXISTENCE);
		}
		Date date = new Date();
		enterpriseExtend.setCreateTime(date);
		enterpriseExtend.setModifyTime(date);
		enterpriseExtend.setId(UuidUtil.uuid());
		enterpriseExtendMapper.insert(enterpriseExtend);
		return new JsonResponse<>();
	}

	@Override
	public JsonResponse<?> getEnterprise(EnterpriseExtend enterpriseExtend) {
		enterpriseExtend = enterpriseExtendMapper.selectByPrimaryKey(enterpriseExtend);
		
		return new JsonResponse<EnterpriseExtend>(enterpriseExtend);
	}

	@Override
	public JsonResponse<?> update(EnterpriseExtend enterpriseExtend) {
		EnterpriseExtend extend = new EnterpriseExtend();
		extend.setTaxpayerNum(enterpriseExtend.getTaxpayerNum());
		extend = enterpriseExtendMapper.selectByPrimaryKey(extend);
		if(null == extend) {
			return new JsonResponse<>(ResponseCodeEnum.DATA_NOT_EXISTENCE);
		}
		enterpriseExtendMapper.updateByPrimaryKeySelective(enterpriseExtend);
		return new JsonResponse<>();
	}
	
	
}
