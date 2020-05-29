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

import lombok.Data;

/**
 * [概 要] 企业常用开票项目表 entity<br/>
 * [环 境] J2SE 1.7
 * 
 * @version 1.0
 */
@Data
public class PreferenceInvoiceItemBean implements Serializable {
	/**
     * Copyright (c) 天畅伟业科技有限公司 
     * 本著作物的著作权归天畅伟业有限公司所有。
     */
    private static final long serialVersionUID = 1L;
	/** 开票项目ID **/
	private String itemId;
	/** 商品名称 **/
	private String commodityName;
	/** 征收品目类型 1：货物劳务 2服务 **/
	private String type;

}
