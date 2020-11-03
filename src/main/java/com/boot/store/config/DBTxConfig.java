package com.boot.store.config;


import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import java.util.Properties;
@Configuration
public class DBTxConfig {

	@Autowired
	private DataSourceTransactionManager transactionManager;

	@NacosValue(value = "${reproduction:false}", autoRefreshed = true)
	private Boolean isReproduction;

	@Bean(name = "txAdvice")
	public TransactionInterceptor getAdvisor(){
		Properties properties = new Properties();
		if (isReproduction){
			properties.setProperty("add*", "readOnly");
			properties.setProperty("save*", "readOnly");
			properties.setProperty("update*", "readOnly");
			properties.setProperty("delete*", "readOnly");
			properties.setProperty("write*", "readOnly");
			properties.setProperty("batch*", "readOnly");
			properties.setProperty("create*", "readOnly");
			properties.setProperty("do*", "readOnly");
			properties.setProperty("edit*", "readOnly");
			properties.setProperty("execute*", "readOnly");
			properties.setProperty("validate*", "readOnly");
			properties.setProperty("export*", "readOnly");
			properties.setProperty("import*", "readOnly");
			properties.setProperty("insert*", "readOnly");
			properties.setProperty("process*", "readOnly");
			properties.setProperty("publish*", "readOnly");
			properties.setProperty("remove*", "readOnly");
			properties.setProperty("submit*", "readOnly");
			properties.setProperty("set*", "readOnly");
			properties.setProperty("onAuthenticationSuccess", "readOnly");
			properties.setProperty("get*", "readOnly");
			properties.setProperty("load*", "readOnly");
			properties.setProperty("query*", "readOnly");
			properties.setProperty("search*", "readOnly");
			properties.setProperty("find*", "readOnly");
			properties.setProperty("*", "readOnly");
		}
		return new TransactionInterceptor(transactionManager, properties);
	}

	@Bean
	public BeanNameAutoProxyCreator txProxy(){
		BeanNameAutoProxyCreator creator=new BeanNameAutoProxyCreator();
		creator.setInterceptorNames("txAdvice");
		creator.setBeanNames("*Service", "*ServiceImpl","*Controller");
		creator.setProxyTargetClass(true);
		return creator;
	}
}
