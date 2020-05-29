package com.cycs.poskp.dto;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.cycs.poskp.beans.EnterpriseBean;

import lombok.Data;
@Data
public class EnterpriseDto implements Serializable {

	/** 实体类序列化id **/
	private static final long serialVersionUID = -71879706101962007L;
	@Valid
	@NotNull(message = "请求参数不能为空")
	private EnterpriseBean serverParameterJson;
	
}
