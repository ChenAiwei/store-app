package com.boot.store.vo.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：SysLog
 * @CreateDate：2020/7/29 14:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SysLogVo implements Serializable {
	private String optUserId;
	private String optUserName;
	private String modelName;
	private String className;
	private String methodName;
	private String requestUrl;
	private String requestParams;
	private String responseBody;
	private Integer optType;
	private Integer optStatus;
	private String timeConsu;
	private String createTime;
}
