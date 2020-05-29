package com.cycs.poskp.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

/**
 * @author <b>孤独的牧羊人</b><br/>
 * @创建时间 <b>2017年12月1日 下午4:55:57</b><br/>
 * @概述<b> 税（费）种认定信息表 </b>
 */
@Data
@JsonInclude(value = Include.NON_NULL)
public class RDSfzrdxxbVO implements Serializable{

	private static final long serialVersionUID = -8798516643888139026L;
	// 认定凭证UUID
	private String rdpzuuid;
	// 登记序号
	private String djxh;
	// 主附税标志||(0主税1附税)
	private String zfsbz;
	// 主税uuid
	private String rdzsuuid;
	// 征收项目代码
	private String zsxmDm;
	// 征收品目代码
	private String zspmDm;
	// 征收品目代码
	private String zspmMc;
	// 征收子目代码
	private String zszmDm;
	// 认定有效期起
	private String rdyxqq;
	// 认定有效期止
	private String rdyxqz;
	// 行业代码
	private String hyDm;
	// 申报期限代码
	private String nsqxDm;
	// 税率或单位税额
	private String slhdwse;
	// 预算科目代码
	private String yskmDm;
	// 征收率
	private String zsl;
	// 预算分配比例代码
	private String ysfpblDm;
	// 收款国库代码
	private String skgkDm;
	// 缴款期限代码
	private String jkqxDm;
	// 征收代理方式代码
	private String zsdlfsDm;
	// 主管税务所（科、分局）代码
	private String zgswskfjDm;
	// 录入人代码
	private String lrrDm;
	// 录入日期
	private String lrrq;
	// 修改人代码
	private String xgrDm;
	// 修改日期
	private String xgrq;
	// 数据归属地区
	private String sjgsdq;
	// 有效标志
	private String yxbz;
	// 征收项目城乡标志
	private String zsxmcxbzDm;
	// 规费经办机构
	private String gfjbjgDm;
	// 缴费级次
	private String jfjcDm;
	// 主税征收项目代码
	private String zszsxmDm;
	// 是否主营征收品目
	private String sfzyzspm;
	// 征收品目类型（1:货物劳务 2:服务）
	private String zspmType;

	/**
	 * 税费种认定信息(附加税)
	 */
	private List<RDSfzrdxxbVO> rdSfzrdxxbVOList;
}
