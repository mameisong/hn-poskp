/**
 * Copyright (c) 天畅伟业科技有限公司 2016
 * 本著作物的著作权归天畅伟业有限公司所有。
 */

package com.cycs.poskp.beans;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
@Data
public class PosInformationBean implements Serializable {
	/**
     * Copyright © 2018 wdq All rights reserved.
     */
    private static final long serialVersionUID = 1L;
    
	/** 
	 * 开票项
	 **/
	private List<PreferenceInvoiceItemBean> InvoiceItem;
	/**
	 * 税费种认定信息
	 */
	private List<RDSfzrdxxbVO> rdSfzrdxxbVOList;
}
