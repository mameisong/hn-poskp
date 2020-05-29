/**
 * Copyright (c) 天畅伟业科技有限公司 2016
 * 本著作物的著作权归天畅伟业有限公司所有。
 */

package com.cycs.poskp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
@Data
@JsonInclude(value = Include.NON_NULL)
public class EnterpriseBean implements Serializable {
	/**
     * Copyright © 2018 wdq All rights reserved.
     */
    private static final long serialVersionUID = 1L;
    private String zspmType;
    private String enterpriseId;
    private String zspmMc;
    
    private String tmpToken;
    private String posId;
    private Integer pageIndex = 1;
    private Integer pageSize = 10000;
    /** 主键ID **/
	private String id;
	/** 企业名称 **/
	private String enterpriseName;
	/** 纳税人识别号 **/
	private String taxpayerNum;
	/** 登记序号 **/
	private String registerNum;
	/** 核定额度 **/
	private BigDecimal amount;
	/** 剩余核定额度 **/
	private BigDecimal remainAmount;
	/** 核定额度单位 **/
	private String amountUnit;
	/** 认证用户名手机号 **/
	private String legalPersonMobile;
	/** 认证用户名名称 **/
	private String legalPersonName;
     /** 认证用户证件号码 **/
     private String legalPersonIdCode;
     /** 认证用户证件类型 **/
     private String legalPersonIdType;
	/** 地址 **/
	private String address;
	/** 电话 **/
	private String contacts;
	/** 开户行账号 **/
	private String bankAccount;
	/** 征收项目代码 **/
	private String zsxmDm;
	/** 征收品目代码 **/
	private String zspmDm;
	/** 征收率 **/
	private String zsl;
	/** 行业代码 **/
	private String hyDm;
	/** 课征主体登记类型代码 **/
	private String kzztdjlxDm;
	/** 街道乡镇代码 **/
	private String jdxzDm;
	/** 生产经营地址行政区划数字代码 **/
	private String scjydzxzqhszDm;
	/** 主管税务所（科、分局）代码 **/
	private String zgswskfjDm;
	/** 登记注册类型代码 **/
	private String djzclxDm;
	/** 企业法人ID **/
	private String legalPersonId;
	/** 税局ID **/
	private String bureauId;
	/** 主管税务局名称 **/
	private String zgswjmc;
	/** 注册标志 **/
	private String registerFlag;
	/** 开票ID **/
	private String kpId;
	// 同步企业数据2 开始 陈誉天 2018-02-23
	/** 企业状态 **/
	private String status;
	// 同步企业数据2 结束 陈誉天 2018-02-23
	/** 企业状态信息 **/
	private String memo;
	/** 创建时间 **/
	private Timestamp createTime;
	/** 修改时间 **/
	private Timestamp modifyTime;
	
	private String enableFlag;
	
	private String showNum;
	/** 
	 * app用户ID
	 **/
	private String appUserId;
	
	/**
	 * 税费种认定信息
	 */
	private List<RDSfzrdxxbVO> rdSfzrdxxbVOList;
}
