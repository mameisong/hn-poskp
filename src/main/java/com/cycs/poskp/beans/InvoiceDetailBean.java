/**
 * Copyright (c) 天畅伟业科技有限公司 2016
 *	 本著作物的著作权归天畅伟业有限公司所有。
 */

package com.cycs.poskp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cycs.poskp.Constants;

import lombok.Data;
@Data
public class InvoiceDetailBean implements Serializable {
	/**
     * Copyright (c) 天畅伟业科技有限公司 
     * 	本著作物的著作权归天畅伟业有限公司所有。
     */
    private static final long serialVersionUID = 1L;
    /** 主键ID **/
	private String id;
	/**商品编码 */
	@NotBlank(message = "商品编码不可为空")
	@Pattern(regexp = "^\\d{0,24}$", message = "商品编码需为纯数字,限1-24个字符")
	private String spbmCode;
	/** 项目名称 **/
	@NotBlank(message = "货物或应税劳务服务名称不可为空")
	@Pattern(regexp = Constants.VALID_VAL + "{0,50})$", message = "货物或应税劳务服务名称不能包含非法字符,限1-50个字符")
	private String xmmc;
	/** 项目金额 **/
	private BigDecimal xmje;
	/** 税率 **/
	private BigDecimal sl;
	/** 税额 **/
	private BigDecimal se;
	/** 规格型号 **/
	@Pattern(regexp = Constants.VALID_VAL + "{0,20})$", message = "规格型号包含非法字符或超出20个字符")
	private String ggxh;
	/** 单位 **/
	@Pattern(regexp = Constants.VALID_VAL + "{0,10})$", message = "单位包含非法字符或超出10个字符")
	private String dw;
	/** 项目数量 **/
	@NotNull(message = "项目数量不能为空")
    @Digits(integer = 8, fraction = 6, message = "项目数量限8位整数,6位小数")
    @DecimalMin(value = "0", inclusive = false, message = "项目数量需大于0")
	private BigDecimal xmsl;
	/** 项目单价 **/
	private BigDecimal xmdj;
	/** 发票ID **/
	private String invoiceId;
	/** 开票项目ID **/
	private String itemId;
	/** 创建时间 **/
	private Timestamp createTime;
	/** 修改时间 **/
	private Timestamp modifyTime;
	/** 含税销售额 **/
	@NotNull(message = "含税销售额不能为空")
    @Digits(integer = 8, fraction = 2, message = "含税销售额限8位整数,2位小数")
    @DecimalMin(value = "0", inclusive = false, message = "含税销售额需大于0")
    private BigDecimal hsxse;

}
