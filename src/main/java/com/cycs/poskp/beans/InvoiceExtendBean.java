package com.cycs.poskp.beans;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class InvoiceExtendBean implements Serializable {
	// 用一句话描述这个变量表示什么
	private static final long serialVersionUID = -780258652122124554L;
	private List<String> serialNumbers;
	private String invoiceId;
	private String userId;
	private String enterpriseId;
	private String posId;
	private String id;
	private Integer role;
	private Integer sffhkpxm;
	private Integer pageIndex;
	private Integer pageSize;
	public InvoiceExtendBean(String userId, String enterpriseId, String id, Integer role,
			Integer sffhkpxm, Integer pageIndex, Integer pageSize) {
		this.userId = userId;
		this.enterpriseId = enterpriseId;
		this.id = id;
		this.role = role;
		this.sffhkpxm = sffhkpxm;
		this.pageIndex = pageIndex;
		this.pageSize = pageSize;
	}
	public InvoiceExtendBean() {}
}
