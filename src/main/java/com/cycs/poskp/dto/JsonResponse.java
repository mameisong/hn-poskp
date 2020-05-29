package com.cycs.poskp.dto;

import java.io.Serializable;

import com.cycs.poskp.enums.ResponseCodeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResponse<T> implements Serializable{
    private static final long serialVersionUID = 5471300085561414420L;
    
    private String code;
    private String message;
    private T data;
    
	public JsonResponse(T data,ResponseCodeEnum errCodeEnum) {
		this.code = errCodeEnum.getCode();
		this.message = errCodeEnum.getMessage();
		this.data = data;
	}
	
	public JsonResponse(ResponseCodeEnum errCodeEnum) {
		this.code = errCodeEnum.getCode();
		this.message = errCodeEnum.getMessage();
	}
	
	public JsonResponse() {
		this.code = ResponseCodeEnum.RESP_OK.getCode();
		this.message = ResponseCodeEnum.RESP_OK.getMessage();
	}
	
	public JsonResponse(T data){
		this.code = ResponseCodeEnum.RESP_OK.getCode();
		this.message = ResponseCodeEnum.RESP_OK.getMessage();
		this.data = data;
	}
	 public JsonResponse(ResponseCodeEnum errCodeEnum, Object ... args) {
	        if(null == errCodeEnum) {
	            //默认成功
	            errCodeEnum = ResponseCodeEnum.RESP_OK;
	        }
	        this.code = errCodeEnum.getCode();
	        this.message = String.format(errCodeEnum.getMessage(), args);
	    }
}
