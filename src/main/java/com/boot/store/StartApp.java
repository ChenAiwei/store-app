package com.boot.store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @Author：chenaiwei
 * @Description：StartApp
 * @CreateDate：2020/7/8 9:00
 */
@SpringBootApplication
@EnableCaching
public class StartApp {
	public static void main(String[] args) {
		SpringApplication.run(StartApp.class, args);
	}
}
