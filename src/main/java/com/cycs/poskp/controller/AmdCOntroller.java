package com.cycs.poskp.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 	爱买单（pos机公司）接口转发Controller
 * @author mameisong
 */
@RestController
@RequestMapping("amd")
public class AmdCOntroller {
	
	@Value("amd.key")
	private String key;

	@PostMapping("selectInfoByPosId")
	public Object selectInfoByPosId() {
		Map<String, String> map = new HashMap<String, String>(); 
		map.put("posId", key);
		map.put("timestamp","tweer");
		return map;
	}
	
}
