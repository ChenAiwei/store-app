package com.boot.store.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.store.dto.RegisterDto;
import com.boot.store.entity.TUser;
import com.boot.store.service.system.ITUserService;
import com.boot.store.utils.MD5Util;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * @Author：chenaiwei
 * @Description：RegisertController
 * @CreateDate：2020/7/9 8:52
 */
@RestController
public class RegisterController {

	@Autowired
	private ITUserService userService;

	@PostMapping("/register")
	public ResultVo<?> save(@RequestBody  @Validated RegisterDto dto) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		TUser tUser = userService.getOne(new QueryWrapper<TUser>().eq("user_name", dto.getUsername()));
		if (null != tUser){
			return ResultVoUtil.error("用户名已存在！");
		}
		TUser user = new TUser();
		user.setUid(UUIDUtils.genUid());
		user.setUserName(dto.getUsername());
		user.setPassWord(MD5Util.getEncryptedPwd(dto.getPassword()));
		user.setCreateTime(new Date());
		userService.save(user);
		return ResultVoUtil.success();
	}
}
