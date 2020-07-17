package com.boot.store.controller.system;

import com.boot.store.dto.auth.UserAuthDto;
import com.boot.store.service.system.IUserAuthService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：UserAuthController
 * @CreateDate：2020/4/3 8:42
 */
@RestController
@RequestMapping("/auth/info")
@Slf4j
public class UserAuthController {

	@Autowired
	private IUserAuthService userAuthService;

	/**
	 * 根据uid获取用户的所有权限信息
	 * @param userId
	 * @return
	 */
	@GetMapping("/list")
	public ResultVo<List<UserAuthDto>> list(@RequestParam String userId){
			return ResultVoUtil.success(userAuthService.info(userId));
	}

	@GetMapping("/listAll")
	public ResultVo<List<UserAuthDto>> listAll(){
		return ResultVoUtil.success(userAuthService.info(null));
	}
}
