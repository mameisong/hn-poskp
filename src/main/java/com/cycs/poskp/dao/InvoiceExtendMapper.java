/**
 *
 * Copyright (c) 北京畅易财税科技有限公司 2019
 * 本著作物的著作权归北京畅易财税有限公司所有
 */
package com.cycs.poskp.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cycs.poskp.beans.InvoiceExtendBean;
import com.cycs.poskp.entity.InvoiceExtend;

@Mapper
public interface InvoiceExtendMapper {
    int deleteByPrimaryKey(String id);

    int insert(InvoiceExtend record) ;

    int insertSelective(InvoiceExtend record);

    List<InvoiceExtend> selectByPrimaryKey(InvoiceExtend record);

    /**
     * 	查询处理中的发票信息
     * @author mameisong
     * @param record
     * @return
     */
    List<InvoiceExtendBean> selectInProcessing(InvoiceExtend record);

    int updateByPrimaryKeySelective(InvoiceExtend record);

    int updateByPrimaryKey(InvoiceExtend record);

	/**
	 * 	根据流水号，机器编号查询发票开具信息
	 * @author mameisong
	 * @param invoiceExtendBean
	 * @return
	 */
	List<InvoiceExtend> getList(InvoiceExtendBean invoiceExtendBean);
}