/**
 * Copyright (c) 天畅伟业科技有限公司 2016
 * 	本著作物的著作权归天畅伟业有限公司所有。
 */

package com.cycs.poskp.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import com.cycs.poskp.Constants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
@Data
@JsonInclude(value = Include.NON_NULL)
public class InvoiceBean implements Serializable {
    /**
     * Copyright (c) 天畅伟业科技有限公司 
     *	 本著作物的著作权归天畅伟业有限公司所有。
     */
    private static final long serialVersionUID = 1L;
    /**pos机id*/
    @NotBlank(message = "机器编号不能为空！")
    private String posId;
    @NotBlank(message = "流水号不能为空！")
    private String serialNumber;
    /** 主键ID **/
    private String id;
    /** 购买方名称*/
    @NotBlank(message = "购买方名称不可以为空")
    @Pattern(regexp = Constants.VALID_VAL + "{0,50})$", message = "购买方名称不能包含非法字符,限1-50个字符")
    private String gmfMc;
    /** 购买方纳税人识别号 **/
    @Pattern(regexp = "^[0-9A-Z]{0,20}$", message = "购买方纳税人识别号不能包含非法字符,限20个字符")
    private String gmfNsrsbh;
    /** 购买方地址电话 **/
    @Pattern(regexp = Constants.VALID_VAL + "{0,50})$", message = "购买方地址电话不能包含非法字符,限50个字符")
    private String gmfDzdh;
    /** 购买方开户行账号 **/
    @Pattern(regexp = Constants.VALID_VAL + "{0,50})$", message = "购买开户行及账号不能包含非法字符,限50个字符")
    private String gmfYhzh;
    /** 合计金额 **/
    private BigDecimal hjje;
    /** 合计税额 **/
    private BigDecimal hjse;
    /** 缴款金额 **/
    private BigDecimal jkje;
    /** 收款人 **/
    private String skr;
    /** 复核人 **/
    private String fhr;
    /** 开票人 **/
    private String kpr;
    /** 企业ID **/
    private String enterpriseId;
    /** 发票状态：0处理中、1开票成功、2开票失败、3开票取消、4未处理     说明：生成发票0、任何失败2、不知道状态3、成功1 */
    private String status;
    /** 发票类型（1-手动，2-非手动）  **/
    private String type;
    /** 开票类型（0蓝字发票，1红字发票）  **/
    private String kplx;
    /** 创建时间 **/
    private Timestamp createTime;
    /** 修改时间 **/
    private Timestamp modifyTime;
    /** 微信OPENID **/
    private String wechatOpenid;
    /** 微信UNIONID **/
    private String wechatUnionid;
    /** 发票请求流水号 **/
    private String fpqqlsh;
    /** 税控设备编号 **/
    private String jqbh;
    /** 发票代码 **/
    private String fpDm;
    /** 发票号码 **/
    private String fpHm;
    /** 开票日期 **/
    private Timestamp kprq;
    /** 发票密文 **/
    private String fpMw;
    /** 校验码 **/
    private String jym;
    /** 二维码 **/
    private String ewm;
    /** 返回代码 **/
    private String returncode;
    /** 返回信息 **/
    private String returnmsg;
    /** 销售方纳税人识别号 **/
    private String xsfNsrsbh;
    /** 销售方名称 **/
    private String xsfMc;
    /** 销售方地址、电话 **/
    private String xsfDzdh;
    /** 销售方银行账号 **/
    private String xsfYhzh;
    /** 原发票代码 **/
    private String yfpDm;
    /** 原发票号码 **/
    private String yfpHm;
    /** 原发票开票日期 **/
    private Timestamp yfpKprq;
    
    /** 价税合计 **/
    private BigDecimal jshj;
    /** 备注 **/
    private String bz;
    /** 备注 **/
    private String resBz;
    /** 是否开具红字发票 **/
    private String sfkjhzfp;
    /** 开票用户id **/
    private String userId;
    /** 税务机关代码 */
    private String swjgdm;
    /** PDF、预览图生成标识 */
    private String imageFlag;
    private String sendType;
    private String mailFlag;
    /**
     * 代开申请UUID
     */
    private String dksquuid;
    
    /**
     * 支付状态：0未处理，1成功、2失败、3处理中
     */
    private String payStatus;
    /**
     * 是否交税：0否、1是
     */
    private String sfjs;
    /**
     * 记录步骤：0未处理、1申请成功、2开票成功、3回写金三成功
     */
    private String recordProcedure;
    /** 发票推送邮箱地址 **/
    private String email;
    
    /** 企业信息 数据 **/
    private String enterpriseInfo;
    /** 登录方式 **/
    private String loginType;
    /** 开票所属终端 **/
    private String terminal;
    
    private String fakeId;
    @Valid
    @NotEmpty(message = "开票项不能为空")
	private List<InvoiceDetailBean> list;
	
	private EnterpriseBean enterprise;
    
}
