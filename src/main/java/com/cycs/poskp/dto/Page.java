package com.cycs.poskp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.pagehelper.PageInfo;

/**
 * 分页统一参数包装类
 */
@SuppressWarnings("rawtypes")
@JsonIgnoreProperties
public class Page<P> extends PageInfo {
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = -9092429461081386862L;
    /**
     * 参数
     */
    private P params;
    /**
     * 流水号
     */
    private String sn;
    /**
     * 排序
     * <pre>
     *     CREATE_TIME DESC, MODIFY_TIME DESC ...
     * </pre>
     */
    private String orderBy;

    public P getParams() {
        return params;
    }

    public void setParams(P params) {
        this.params = params;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}