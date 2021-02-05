package com.boot.store;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigType;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author：chenaiwei
 * @Description：StartApp
 * @CreateDate：2020/7/8 9:00
 */
@SpringBootApplication
@EnableCaching
@EnableScheduling
@NacosPropertySource(dataId = "${spring.application.name}-${spring.profiles.active}.properties",type = ConfigType.PROPERTIES,groupId = "${spring.profiles.active}",autoRefreshed = true)
@Slf4j
public class StartApp {
	@NacosInjected
	private NamingService namingService;

	@Value("${spring.application.name}")
	private String applicationName;

	@Value("${server.port}")
	private int serverPort;

	@PostConstruct
	public void registerInstance() throws NacosException {
		InetAddress localHost = null;
		try {
			localHost = Inet4Address.getLocalHost();
		} catch (UnknownHostException e) {
			log.error(e.getMessage(),e);
		}
		namingService.registerInstance(applicationName, localHost.getHostAddress(), serverPort);
	}
	public static void main(String[] args) {
		SpringApplication.run(StartApp.class, args);
	}
}
