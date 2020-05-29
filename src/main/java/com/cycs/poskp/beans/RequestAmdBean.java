/**
 * Copyright (c) 天畅伟业科技有限公司 2016
 * 本著作物的著作权归天畅伟业有限公司所有。
 */

package com.cycs.poskp.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
@Data
@JsonInclude(value = Include.NON_NULL)
public class RequestAmdBean implements Serializable {
	/**实体类序列化id*/
	private static final long serialVersionUID = 5651181800062407399L;
	private String posId;
	
	private String timestamp;
	
	private String sign;

	private String flag;

	private String message;
	
	public RequestAmdBean(String posId, String sign, String timestamp) {
		this.posId = posId;
		this.timestamp = timestamp;
		this.sign = sign;
	}
	
	public RequestAmdBean() {
		
	}

	public RequestAmdBean(String posId, String timestamp, String sign, String flag, String message) {
		this.posId = posId;
		this.timestamp = timestamp;
		this.sign = sign;
		this.flag = flag;
		this.message = message;
	}
	
	
}
