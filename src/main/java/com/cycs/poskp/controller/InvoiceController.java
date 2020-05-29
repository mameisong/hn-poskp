package com.cycs.poskp.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cycs.poskp.Constants;
import com.cycs.poskp.dto.EnterpriseDto;
import com.cycs.poskp.dto.InvoiceInfoDto;
import com.cycs.poskp.dto.InvoiceQueryDto;
import com.cycs.poskp.dto.JsonResponse;
import com.cycs.poskp.entity.InvoiceExtend;
import com.cycs.poskp.enums.ResponseCodeEnum;
import com.cycs.poskp.service.IInvoiceService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("invoice")
@Slf4j
public class InvoiceController {
	@Autowired
	private IInvoiceService invoiceService;
	
	/**
	 * 	发票开具
	 * @author mameisong
	 * @param invoiceDto
	 * @param bindingResult
	 * @return
	 */
	@PostMapping
	public JsonResponse<?> invoice(@RequestBody @Valid InvoiceInfoDto invoiceDto,BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
            final String errorStr = bindingResult.getFieldErrors().parallelStream()
                    .map(e -> e.getField() + Constants.D.COLON + e.getDefaultMessage())
                    .collect(Collectors.joining(Constants.D.COMMA));
            log.info("开票基础验证未通过, accessKeyId:{}, msg:{}",invoiceDto.getServerParameterJson().getPosId(), errorStr);
            return new JsonResponse<>(ResponseCodeEnum.PARAM_INVALID, errorStr);
        }
		return invoiceService.invoice(invoiceDto);
	}
	
	@PostMapping("applyForInvoice")
	public JsonResponse<?> applyForInvoice(@RequestBody @Valid EnterpriseDto enterpriseDto) {
		
		return invoiceService.applyForInvoice(enterpriseDto);
	}
	/**
	 * 	根据流水号，机器编号查询发票开具信息
	 * @author mameisong
	 * @param page
	 * @return
	 */
	@PostMapping("getList")
	public JsonResponse<List<InvoiceExtend>> getList(@RequestBody InvoiceQueryDto invoiceQueryDto) {
		return invoiceService.getList(invoiceQueryDto);
	}
}
