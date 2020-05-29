package com.cycs.poskp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cycs.poskp.dto.JsonResponse;
import com.cycs.poskp.entity.EnterpriseExtend;
import com.cycs.poskp.service.IEnterpriseService;

@RestController
@RequestMapping("enterprise")
public class EnterpriseController {
	
	@Autowired
	private IEnterpriseService enterpriseService;
	
	/**
	 * 	添加企业信息
	 * @author wangrongxin
	 */
	@PostMapping("add")
	public JsonResponse<?> add(@RequestBody EnterpriseExtend enterpriseExtend) {
		return enterpriseService.add(enterpriseExtend);
	}
	
	/**
	 * 	修改企业信息
	 * @author wangrongxin
	 */
	@PostMapping("update")
	public JsonResponse<?> update(@RequestBody EnterpriseExtend enterpriseExtend) {
		return enterpriseService.update(enterpriseExtend);
	}
	
	/**
	 * 	查询企业信息
	 * @author wangrongxin
	 */
	@PostMapping("getEnterprise")
	public JsonResponse<?> getEnterprise(@RequestBody EnterpriseExtend enterpriseExtend) {
		return enterpriseService.getEnterprise(enterpriseExtend);
	}
}
