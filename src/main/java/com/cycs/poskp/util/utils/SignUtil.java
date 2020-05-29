package com.cycs.poskp.util.utils;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.springframework.util.DigestUtils;

public class SignUtil {
	public static final String data_format = "yyyyMMddHHmmss";
	
	public static String sign(Map<String, String> signMap,String key){
		
		// 对参数key进行排序
		Map<String, String> sortMap = new TreeMap<String, String>();
		sortMap.putAll(signMap);
					
		// 拼接待签名串
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : sortMap.entrySet()) {
		sb.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		String signStr = sb.toString();
		//去掉前面的&号
		signStr = signStr.substring(1);
		signStr += "&key=" + key;
		String sign = DigestUtils.md5DigestAsHex(signStr.getBytes());

		return sign;
	}
	
	public static String dateFormat() {
		DateFormat df = new SimpleDateFormat(data_format);
		return df.format(new Date());
	}
	
}
