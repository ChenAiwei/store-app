package com.boot.store.controller.system;

import com.boot.store.service.system.ISysLogService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import com.boot.store.vo.system.SysLogQueryParamsVo;
import com.boot.store.vo.system.SysLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：chenaiwei
 * @Description：SysLogController
 * @CreateDate：2020/7/29 14:48
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

	@Autowired
	private ISysLogService sysLogService;

	@PostMapping("/list")
	public ResultVo<PageVo<SysLogVo>> list(@RequestBody SysLogQueryParamsVo queryParamsVo){
		return ResultVoUtil.success(sysLogService.listCustom(queryParamsVo));
	}
}
