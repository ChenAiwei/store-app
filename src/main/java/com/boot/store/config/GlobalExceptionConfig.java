package com.boot.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author：chenaiwei
 * @Description：GlobalExceptionConfig
 * @CreateDate：2020/4/3 16:29
 */
@Configuration
public class GlobalExceptionConfig {
	@Bean
	public RestExceptionHandler getHandlerExceptionResolver() {
		return new RestExceptionHandler();
	}
}
