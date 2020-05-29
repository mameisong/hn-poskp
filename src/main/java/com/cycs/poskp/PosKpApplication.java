/**
 * Copyright (c) 北京畅易财税科技有限公司 2019
 * 	本著作物的著作权归北京畅易财税有限公司所有
 */

package com.cycs.poskp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;

/**
 * 	批量开票数据服务启动类
 * @author mameisong
 */
@Slf4j
@SpringBootApplication
@EnableFeignClients
@ComponentScan("com.cycs")
@EnableScheduling
public class PosKpApplication {
	public static void main(String[] args) {
		try {
			SpringApplication.run(PosKpApplication.class, args);
		} catch (Exception e) {
			log.error("批量开票数据服务启动失败：",e.getMessage());
		}
	}
}
