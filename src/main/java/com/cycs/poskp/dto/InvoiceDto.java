package com.cycs.poskp.dto;

import com.cycs.poskp.beans.InvoiceBean;

import lombok.Data;
@Data
public class InvoiceDto extends InvoiceBean {

	/**实体类序列化id*/
	private static final long serialVersionUID = 9175761151689198161L;
	
	private String posId;

	private String token;
	
}
