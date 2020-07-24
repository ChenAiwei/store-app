package com.boot.store.service.impl;

import com.boot.store.entity.SysLog;
import com.boot.store.service.AsyncService;
import com.boot.store.service.system.ISysLogService;
import com.boot.store.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：AsyncServiceImpl
 * @CreateDate：2020/7/13 10:12
 */
@Service
public class AsyncServiceImpl implements AsyncService {
	@Autowired
	private ISysLogService sysLogService;

	@Async("asyncExecutor")
	@Override
	public void executeAsyncLog(String userId, String option, String className, String methodName, String url, Object[] args, Object o, Integer type, long l,Integer status) {
		List<Object> objectsList = Arrays.asList(args);
		String requestParamsStr = JsonUtils.objectToJson(objectsList);
		String responseStr = JsonUtils.objectToJson(o);
		SysLog sysLog = SysLog.builder().userId(userId)
				.modelName(option)
				.className(className)
				.methodName(methodName)
				.requestUrl(url)
				.requestParams(requestParamsStr)
				.responseBody(responseStr)
				.optType(type)
				.optStatus(status)
				.timeConsu(l)
				.createTime(new Date())
				.build();
		sysLogService.save(sysLog);
	}
}
