package com.cycs.poskp.beans;

import java.io.Serializable;

/**
 * @author sky
 *
 */
public class JsonResult implements Serializable {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6952922297949129810L;

	/**
	 * 结果标志(1:异常 0:正常)<br/>
	 * 默认0
	 */
	private String flag = "0";

	/**
	 * 消息
	 */
	private String message = null;

	private Object object = null;

	private Object generatedId = null;

	public void fail(String message) {
		this.flag = "1";
		this.message = message;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public boolean isSuccess() {
		return "0".equals(this.flag);
	}

	public Object getGeneratedId() {
		return generatedId;
	}

	public void setGeneratedId(Object generatedId) {
		this.generatedId = generatedId;
	}
}
