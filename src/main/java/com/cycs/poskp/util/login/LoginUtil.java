/**
 * Copyright (c) 北京畅易财税科技有限公司 2019
 * 本著作物的著作权归北京畅易财税有限公司所有
 */
package com.cycs.poskp.util.login;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.cycs.common.redis.cache.service.RedisService;
import com.cycs.poskp.Constants;
import com.cycs.poskp.beans.JsonResult;
import com.cycs.poskp.beans.TAppUserBean;
import com.cycs.poskp.util.okhttp.OkHttpUtil;

/**
 * 公共登录方法
 * @author wangyao
 */
@Component
public class LoginUtil {
	private static Logger logger = LoggerFactory.getLogger(LoginUtil.class);
	@Value("${user.userName}")
	private String userName;
	@Value("${user.password}")
	private String password;
	@Value("${http.url.dkurl}")
	private String dkUrl;
	@Autowired
	private RedisService rs;
	@Autowired
	private OkHttpUtil okHttp;
	/**
	 * 
	 * 登录方法
	 * @author wangyao
	 * @return flag （0：成功 1：失败）
	 */
	public String login() {
		try {
			TAppUserBean appUser = new TAppUserBean();
			appUser.setUserName(userName);
			appUser.setPassword(password);
			appUser.setAppSoftToken("1");
			String param = JSON.toJSONString(appUser);
			Map<String, String> map = Collections.singletonMap("data", param);
			String loginUrl = String.format(dkUrl, "enterprise\\login");
			String response = okHttp.postForm(loginUrl , map);
			if(StringUtils.isNotBlank(response)) {
				JsonResult result = JSON.parseObject(response, JsonResult.class);
				if("0".equals(result.getFlag())) {
					List<TAppUserBean> list = JSON.parseArray(result.getObject().toString(), TAppUserBean.class);
					String token = list.get(0).getToken();
					rs.valueOps().set(Constants.CACHE_KEY_POSKP_PRE, token);
				}
				return result.getFlag();
			}else {
				return "1";
			}
		} catch (Exception e) {
			logger.error("登录出现异常>>>>>>>",e);
			return "1";
		}
	}
}
