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
public class InvoiceExtend implements Serializable {
    /**
     * null
     */
    private String id;

    /**
     * 发票id
     */
    private String invoiceId;

    /**
     * 流水号
     */
    private String serialNumber;

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
     * INVOICE_EXTEND
     */
    private static final long serialVersionUID = 1L;
    
    /**0-处理中 1-开票成功 2-开票失败 3-开票取消 4-未处理*/
    private Integer status;
    
    /**下载地址*/
    private String downloadUrl;
    
    /**错误原因*/
    private String errorMessage;
    
    /**pos机机器编号*/
    private String posId;
    
    public InvoiceExtend(String id, Integer status, String downloadUrl, String errorMessage) {
    	this.id = id;
    	this.status = status;
    	this.downloadUrl = downloadUrl;
    	this.errorMessage = errorMessage;
	}
    public InvoiceExtend() {}
}