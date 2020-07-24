package com.boot.store.service;

/**
 * @Author：chenaiwei
 * @Description：AsyncService
 * @CreateDate：2020/7/13 10:11
 */
public interface AsyncService {

	/**
	 * 异步保存系统日志
	 * @param userId
	 * @param option
	 * @param className
	 * @param methodName
	 * @param url
	 * @param args
	 * @param o
	 * @param type
	 * @param l
	 * @param status
	 */
	void executeAsyncLog(String userId, String option, String className, String methodName, String url, Object[] args, Object o, Integer type, long l,Integer status);
}
