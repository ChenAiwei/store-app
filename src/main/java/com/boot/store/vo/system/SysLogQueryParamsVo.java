package com.boot.store.vo.system;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：SysLogQueryParamsVo
 * @CreateDate：2020/7/29 14:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysLogQueryParamsVo implements Serializable {
	private String startTime;
	private String endTime;
	private String optUserName;
	private Integer optType;
	private Integer optStatus;
	@NotNull(message = "page不能为空！")
	@Min(value = 1,message = "页码最小为1")
	private Integer page;
	@NotNull(message = "limit不能为空！")
	@Min(value = 1,message = "页数最小为1")
	private Integer limit;
}
