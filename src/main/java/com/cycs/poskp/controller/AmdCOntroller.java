package com.cycs.poskp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.cycs.poskp.beans.RequestAmdBean;
import com.cycs.poskp.dto.JsonResponse;
import com.cycs.poskp.util.okhttp.OkHttpUtil;
import com.cycs.poskp.util.utils.SignUtil;

/**
 * 	爱买单（pos机公司）接口转发Controller
 * @author mameisong
 */
@RestController
@RequestMapping("amd")
public class AmdCOntroller {

	@Value("${http.url.amd-api}")
	private String admApiUrl;
	
	@Value("${amd.key}")
	private String admKey;
	
	@Autowired
	private OkHttpUtil okHttp;
	
	@PostMapping("selectInfoByPosId")
	public JsonResponse<?> selectInfoByPosId(@RequestBody RequestAmdBean ext) {
		String timestamp = SignUtil.dateFormat();
		ext.setTimestamp(timestamp);
		
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("posId", ext.getPosId());
		map.put("timestamp",timestamp);
		String sign = SignUtil.sign(map, admKey);
		ext.setSign(sign);
		String url = String.format(admApiUrl, "invoice/selectInfoByPosId");
		String resp = okHttp.postJson(url, JSON.toJSONString(ext));
		return new JsonResponse<>(resp);
	}
	@PostMapping("posRegisterCallBack")
	public JsonResponse<?> posRegisterCallBack(@RequestBody RequestAmdBean ext) {
		String timestamp = SignUtil.dateFormat();
		Map<String, String> signMap = new HashMap<String, String>();
		signMap.put("posId", ext.getPosId());
		signMap.put("flag", "HMACSHA1");
		signMap.put("message", "DEF");
		signMap.put("timestamp", timestamp);
		String sign = SignUtil.sign(signMap, admKey);
		ext.setSign(sign);
		ext.setTimestamp(timestamp);
		String url = String.format(admApiUrl, "invoice/posRegisterCallBack");
		String resp = okHttp.postJson(url, JSON.toJSONString(ext));
		return new JsonResponse<>(resp);
	}
	
}
