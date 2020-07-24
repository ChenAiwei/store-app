package com.boot.store.config;

import com.boot.store.annotation.Log;
import com.boot.store.exception.TokenException;
import com.boot.store.service.AsyncService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Author：chenaiwei
 * @Description：AopConfig
 * @CreateDate：2020/7/10 8:41
 */
@Configuration
@Aspect
@Slf4j
public class AopConfig {

	@Autowired
	private AsyncService asyncService;
	private static final Integer OPT_SUCCESS = 1;
	private static final Integer OPT_ERROR = 0;

	@Pointcut("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
	public void pointcut() {}

	@Around("pointcut() && (@within(com.boot.store.annotation.Log) || @annotation(com.boot.store.annotation.Log))")
	public Object simpleAop(final ProceedingJoinPoint joinPoint) throws Throwable {
		Long before = System.currentTimeMillis();
		String className = joinPoint.getTarget().getClass().getName();
		MethodSignature methodSignature =(MethodSignature) joinPoint.getSignature();
		String methodName = methodSignature.getName();
		Method method = joinPoint.getTarget().getClass().getMethod(methodName, methodSignature.getParameterTypes());
		Log annotation = method.getAnnotation(Log.class);
		Object[] args = joinPoint.getArgs();
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String url = request.getRequestURL().toString();
		String userId = request.getHeader("userId");
		String token = request.getHeader("token");
		log.info("请求模块：【{}】,请求URL:【{}】,请求参数:【{}】,请求头：userId=={}，token=={}", annotation.option(),url, args,userId,token);
		log.info("调用类：{}",className);
		log.info("调用方法：{}",methodName);
		try {
			Object o = joinPoint.proceed();
			Long after = System.currentTimeMillis();
			log.info("共耗时：" + (after - before) + "毫秒");
			log.info("方法返回：return:" + o);
			asyncService.executeAsyncLog(userId,annotation.option(),className,methodName,url,args,o,annotation.type().getType(),(after - before),OPT_SUCCESS);
			return o;
		} catch (Throwable throwable) {
			Long after = System.currentTimeMillis();
			asyncService.executeAsyncLog(userId,annotation.option(),className,methodName,url,args,throwable.getMessage(),annotation.type().getType(),(after - before),OPT_ERROR);
			throw throwable;
		}
	}

	@Before("@within(com.boot.store.annotation.Log) || @annotation(com.boot.store.annotation.Log)")
	public void doBefore(JoinPoint joinPoint) throws TokenException {
	}

	@After("@within(com.boot.store.annotation.Log) || @annotation(com.boot.store.annotation.Log)")
	public void doAfter() {}
}
