package com.cycs.poskp.service;

import com.cycs.poskp.dto.JsonResponse;
import com.cycs.poskp.entity.EnterpriseExtend;

/**
 * 	企业信息接口类
 * @author mameisong
 */
public interface IEnterpriseService {
	
	/**
	 * 	添加企业信息
	 * @author wangrongxin
	 */
	JsonResponse<?> add(EnterpriseExtend enterpriseExtend);

	/**
	 * 	查询企业信息
	 * @author wangrongxin
	 */
	JsonResponse<?> getEnterprise(EnterpriseExtend enterpriseExtend);
	/**
	 * 	修改企业信息
	 * @author wangrongxin
	 */
	JsonResponse<?> update(EnterpriseExtend enterpriseExtend);

}
