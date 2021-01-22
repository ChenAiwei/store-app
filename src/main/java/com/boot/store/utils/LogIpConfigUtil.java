package com.boot.store.utils;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Author：chenaiwei
 * @Description：LogIpConfigUtil
 * @CreateDate：2020/12/22 17:16
 */
@Component
@Slf4j
public class LogIpConfigUtil extends ClassicConverter {
	private static String serverIp = "";

	@Override
	public String convert(ILoggingEvent event) {
		if (StringUtils.isNotBlank(serverIp)){
			return serverIp;
		}
		try {
			Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			while (allNetInterfaces.hasMoreElements()) {
				NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
				if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
					continue;
				} else {
					Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
					while (addresses.hasMoreElements()) {
						ip = addresses.nextElement();
						if (ip != null && ip instanceof Inet4Address) {
							serverIp = ip.getHostAddress();
							return ip.getHostAddress();
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("IP地址获取失败" + e.toString());
		}
		return "";
	}
}
