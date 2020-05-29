/**
 * Copyright (c) 北京畅易财税科技有限公司 2019
 * 	本著作物的著作权归北京畅易财税有限公司所有
 */

package com.cycs.poskp.dto;


import java.io.Serializable;

import com.cycs.poskp.beans.InvoiceExtendBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
/**
 * 	发票信息请求参数Dto
 * @author mameisong
 */
@Data
@JsonInclude(value = Include.NON_NULL)
public class InvoiceQueryDto implements Serializable{
	
	/**实体类序列化id */
	private static final long serialVersionUID = 3720913580025676850L;

	private InvoiceExtendBean serverParameterJson;

}
