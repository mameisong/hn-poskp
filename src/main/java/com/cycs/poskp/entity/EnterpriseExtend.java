/**
 *
 * Copyright (c) 北京畅易财税科技有限公司 2019
 * 本著作物的著作权归北京畅易财税有限公司所有
 */
package com.cycs.poskp.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(value = Include.NON_NULL)
public class EnterpriseExtend implements Serializable {
    /**
     * id
     */
    private String id;

    /**
     * 企业id
     */
    private String enterpriseId;

    /**
     * 收款人
     */
    private String skr;

    /**
     * 复核人
     */
    private String fhr;

    /**
     * 销售方地址
     */
    private String xsfDz;

    /**
     * 销售方电话
     */
    private String xsfDh;

    /**
     * 销售方银行账号
     */
    private String xsfYhzh;

    /**
     * 身份证号
     */
    private String idCode;

    /**
     * pos机id
     */
    private String posId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 是否删除 0否 1是
     */
    private String valid;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 开票人
     */
    private String kpr;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 纳税人识别号
     */
    private String taxpayerNum;

    /**
     * 销售方银行
     */
    private String xsfYh;

    /**
     * ENTERPRISE_EXTEND
     */
    private static final long serialVersionUID = 1L;
    
	public EnterpriseExtend(String posId) {
		this.posId = posId;
	}
	public EnterpriseExtend() {
	}
}