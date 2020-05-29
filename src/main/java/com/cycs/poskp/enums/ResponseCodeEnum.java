package com.cycs.poskp.enums;

public enum ResponseCodeEnum {

	RESP_OK("P0000", "成功"),
	RESP_ERROR("P0001", "系统异常，请稍后再试。"), 
	INCOMPLETE_INFORMATION("P0002", "企业信息不全，请登录APP进行维护。"),
	ZSPMTYPE_NOT_AGREEMENT("P0003", "增收品目类型不一致。"),
	PARAM_INVALID("P0004","请求的参数非法,请参考[%s]"),
	REPEATED_SUBMIT("P0005","数据重复提交。"),
	DATA_EXISTENCE("P0006","数据已经存在，请勿重复提交！"),
	DATA_NOT_EXISTENCE("P0007","请求的数据不存在或已经被删除！")
    ;
    
    
    
    
    
    
   
    
    private String code;

    private String message;

    /**
     * 构造函数初始化值
     * @param code
     * @param message
     */
    private ResponseCodeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取key.
     * @return int
     */
    public String getCode() {
        return code;
    }

    /**
     * 获取错误信息.
     * @return String
     */
    public String getMessage() {
        return message;
    }
}
