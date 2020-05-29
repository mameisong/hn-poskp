package com.cycs.poskp.service;

import java.util.List;

import com.cycs.poskp.dto.EnterpriseDto;
import com.cycs.poskp.dto.InvoiceInfoDto;
import com.cycs.poskp.dto.InvoiceQueryDto;
import com.cycs.poskp.dto.JsonResponse;
import com.cycs.poskp.entity.InvoiceExtend;

/**
 * 	发票开具接口类
 * @author mameisong
 */
public interface IInvoiceService {

	/**
	 * 	发票开具
	 * @author mameisong
	 * @param invoiceDto
	 * @return
	 */
	JsonResponse<?> invoice(InvoiceInfoDto invoiceDto);
	/**
	 * 
	 * 	申请开票
	 * @author wangyao
	 * @param enterpriseDto
	 * @return JsonResponse
	 */
	JsonResponse<?> applyForInvoice(EnterpriseDto enterpriseDto);
	/**
	 * 	根据流水号，机器编号查询发票开具信息
	 * @author mameisong
	 * @param page
	 * @return
	 */
	JsonResponse<List<InvoiceExtend>> getList(InvoiceQueryDto invoiceQueryDto);
	
}
