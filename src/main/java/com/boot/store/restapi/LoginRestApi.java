package com.boot.store.restapi;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.store.dto.LoginDto;
import com.boot.store.dto.auth.*;
import com.boot.store.entity.TUser;
import com.boot.store.exception.ServiceException;
import com.boot.store.service.system.ITUserService;
import com.boot.store.service.system.IUserAuthService;
import com.boot.store.utils.*;
import com.boot.store.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：chenaiwei
 * @Description：LoginRestApi
 * @CreateDate：2020/7/9 17:02
 */
@RestController
@Slf4j
public class LoginRestApi {

	@Autowired
	private ITUserService userService;

	@Autowired
	private IUserAuthService userAuthService;

	@Autowired
	EhcacheUtil cacheUtil;

	@PostMapping("/login")
	public ResultVo<UserTokenDto> loginV1(@RequestBody @Validated (ValidationGroups.LoginV1.class) LoginDto dto, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String cacheCode = cacheUtil.get(dto.getCodeKey());
		if (StringUtils.isBlank(cacheCode)){
			return ResultVoUtil.error("验证码过期，请刷新");
		}
		else if (!cacheCode.equalsIgnoreCase(dto.getCaptcha())){
			return ResultVoUtil.error("验证码错误");
		}
		UserTokenDto tokenDto = this.login(dto,request);
		return ResultVoUtil.success(tokenDto);
	}

	@PostMapping("/login/v2")
	public ResultVo<UserTokenDtoV2> loginV2(@RequestBody @Validated LoginDto dto, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		UserTokenDtoV2 userTokenDtoV2 = new UserTokenDtoV2();
		UserInfoDtoV2 userInfoDtoV2 = new UserInfoDtoV2();
		UserTokenDto tokenDto = this.login(dto,request);
		List<String> menuLevelList = userAuthService.menuLevelList(tokenDto.getUserId());
		userTokenDtoV2.setToken(tokenDto.getToken());
		userInfoDtoV2.setUserId(tokenDto.getUserId());
		userInfoDtoV2.setUserName(tokenDto.getUserName());
		userInfoDtoV2.setMenuLevel(menuLevelList);
		userTokenDtoV2.setUserInfo(userInfoDtoV2);
		return ResultVoUtil.success(userTokenDtoV2);
	}

	private UserTokenDto login(LoginDto dto, HttpServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		TUser tUser = userService.getOne(new QueryWrapper<TUser>().eq("user_name", dto.getUserName()));
		if (null == tUser){
			throw new ServiceException("用户不存在！");
		}
		if (!tUser.getStatus()){
			throw new ServiceException("用户已禁用！");
		}
		boolean validPassword = MD5Util.validPassword(dto.getPassWord(), tUser.getPassWord());
		if (!validPassword){
			throw new ServiceException("密码错误！");
		}
		tUser.setLastLoginTime(new Date());
		Integer loginCount = tUser.getLoginCount();
		tUser.setLoginCount(null != loginCount ? ++loginCount : 1);
		tUser.setLastLoginIp(IpUtil.getIpAddress(request));
		tUser.updateById();
		String token = UUIDUtils.genToken(tUser.getUid());
		UserTokenDto tokenDto = new UserTokenDto(tUser.getUid(),tUser.getUserName(),tUser.getAvatar(),token);
		cacheUtil.set(tUser.getUid(),token,60*60*12);
		log.info("用户{}登录成功,保存token："+token,tUser.getUserName());
		return tokenDto;
	}

	@GetMapping("/logout/{userId}")
	public ResultVo<?> logout(@PathVariable String userId){
		cacheUtil.remove(userId);
		log.info("用户{}登出成功",userId);
		return ResultVoUtil.success();
	}
}
