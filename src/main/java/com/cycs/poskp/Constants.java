/**
 * Copyright (c) 北京畅易财税科技有限公司 2019
 * 本著作物的著作权归北京畅易财税有限公司所有
 */
package com.cycs.poskp;

/**
 * 全局常量 
 * @author liuyuan
 */
public class Constants {
	/**
	 * 允许输入的字符集
	 * ^[\u4e00-\u9fa5\w\s`~!@#$%^&*()-……（） 。》《+=|{}\[\]:;.<>/\\?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]+$
	 * 20190816 删除&
	 */
	public static final String VALID_VAL = "^(?!.*?<\\/?[\\s\\S]*?(?:\".*\")*>)([\\u4e00-\\u9fa5\\w\\s`~!@#$%^*()-……（） 。》《+=|{}\\[\\]:;.<>/\\\\?~！@#￥%……*（）——+|{}【】‘；：”“’。，、？]";

	/**
	 *	token缓存key前缀
	 */
	public static final String CACHE_KEY_POSKP_PRE = "token_pos";
	public static final String TEMPTOKEN_KEY_POSKP_PRE = "token_temp_%s";
	/**税费种认定信息缓存key前缀*/
	public static final String SFZ_KEY_POSKP_PRE = "sfz_pos_%s";
    /**
     * 默认编码
     */
    public static final String DEF_ENCODING = "UTF-8";
    
    public static final String TYPE_ONE = "1";
    public static final String TYPE_TWO = "2";
    /**
     * 成功
     */
    public static final Integer SUCCESS = 1;
    /**
     * 失败
     */
    public static final Integer FAILURE = 0;
    /**
     * 合法
     */
    public static final Integer VALID = 1;
    /**
     * 非法
     */
    public static final Integer INVALID = 0;
    /**
     * 是
     */
    public static final Integer YES = 1;
    /**
     * 否
     */
    public static final Integer NO = 0;
    /**
     * 开
     */
    public static final Integer ON = 1;
    /**
     * 关
     */
    public static final Integer OFF = 0;

    /**
     * 常用 分隔符(Delimiter)
     */
    public static class D {
        /**
         * 字符 逗号
         */
        public static final char C_COMMA = ',';
        /**
         * 字符串 逗号
         */
        public static final String COMMA = ",";
        /**
         * 字符 点
         */
        public static final String DOT = ".";
        /**
         * 字符 中划线
         */
        public static final String LINE = "-";
        /**
         * 下划线
         */
        public static final String UNDERLINE = "_";
        /**
         * 字符串间隔符
         */
        public static final String SEMICOLON  = ";";
        /**
         * 字符串间隔符
         */
        public static final char COLON  = ':';
        /**
         * 百分号
         */
        public static final char C_PERCENT = '%';
        public static final String PERCENT = "%";
        /**
         * 问号
         */
        public static final String QUESTION = "?";
        /**
         * 斜线
         */
        public static final String DIAGONAL = "/";
        /**
         * 和
         */
        public static final char C_AND = '&';
        /**
         * 字符串 和
         */
        public static final String AND = "&";
        /**
         * 等于
         */
        public static final char C_EQUAL = '=';
        /**
         * 字符串 等于
         */
        public static final String EQUAL = "=";
        /**
         * 空格
         */
        public static final String SPACE_SINGLE = " ";

        /**
         * 私有构造
         */
        private D() {
        }
    }

    /**
     *  私有构造
     */
    private Constants() {}
}