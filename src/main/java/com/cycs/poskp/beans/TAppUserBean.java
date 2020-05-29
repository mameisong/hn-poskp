/**
 * Copyright (c) 天畅伟业科技有限公司 2016
 * 本著作物的著作权归天畅伟业有限公司所有。
 * -----------------------------------------------
 * 修改记录 : 
 * 日 期			 版本		 修改人		 修改内容
 * 2017年7月13日	0.1		wangyao		初版作成
 */

package com.cycs.poskp.beans;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

/**
 * [概 要] APP用户表 实体类<br/>
 * [环 境] J2SE 1.7
 * 
 * @author 研发部-wangyao
 * @version 1.0
 */
@Data
public class TAppUserBean implements Serializable {
	private static final long serialVersionUID = 1132890259237789435L;
	/** 主键ID **/
	private String id;
	/** 用户名 **/
	private String userName;
	/** 密码 **/
	private String password;
	/** 姓名 **/
	private String name;
	/** 真实姓名 **/
	private String realName;
	/** 角色 1、管理员 2、开票员 **/
	private String role;
	/** 删除标识：0、否 1、是 **/
	private String delFlag;
	/** 完成注册标识：0、否 1、是 **/
	private String completeFlag;
	/** 创建时间 **/
	private Timestamp createTime;
	/** 修改时间 **/
	private Timestamp modifyTime;
	/** 密码修改时间 **/
	private Timestamp passModifyTime;
	/** 身份证 **/
	private String idCode;
	/** 证件类型 **/
	private String idType;

	private String parentId;

	/** 注册方式 **/
	private String registerType;
	/**
	 * 机器编码
	 */
	private String appSoftToken;
	/**
	 * 登录后生产的身份验证码
	 */
	private String token;
}
