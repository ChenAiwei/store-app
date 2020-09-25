package com.boot.store.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.UserDto;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.dto.system.UserChangePwdDto;
import com.boot.store.entity.TUser;
import com.boot.store.entity.TUserRole;
import com.boot.store.enums.LogEnum;
import com.boot.store.service.system.ITUserRoleService;
import com.boot.store.service.system.ITUserService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import com.boot.store.vo.user.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author：chenaiwei
 * @Description：UserController
 * @CreateDate：2020/4/2 16:08
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

	@Autowired
	private ITUserService userService;
	@Autowired
	private ITUserRoleService userRoleService;
	

	@GetMapping("/queryByUserId/{uid}")
	public ResultVo<UserDto> queryByUserId(@PathVariable String uid){
		return ResultVoUtil.success(userService.queryByUserId(uid));
	}

	@Log(option = "删除用户",type = LogEnum.DEL)
	@PostMapping("/delUser")
	@Transactional(rollbackFor = Exception.class)
	public ResultVo<?> delUser(@RequestBody List<String> idList){
		List<String> list = idList.stream().filter(id -> StringUtils.isNotBlank(id)).collect(Collectors.toList());
		userRoleService.remove(new QueryWrapper<TUserRole>().in("user_id",list));
		userService.removeByIds(list);
		return ResultVoUtil.success();
	}

	@Log(option = "添加用户",type = LogEnum.ADD)
	@PostMapping("/addUser")
	public ResultVo<?> addUser(@Validated(ValidationGroups.Register.class) @RequestBody UserEditVo userVo) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		userService.saveUser(userVo);
		return ResultVoUtil.success();
	}

	@Log(option = "编辑用户",type = LogEnum.EDIT)
	@PostMapping("/editUser")
	public ResultVo<String> editUser(@Validated(ValidationGroups.Editer.class) @RequestBody UserEditVo userVo) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		userService.editUser(userVo);
		return ResultVoUtil.success();
	}

	@PostMapping("/userStatus")
	@Log(option = "更改用户状态",type = LogEnum.EDIT)
	public ResultVo<?> userStatus(@Validated @RequestBody UserStatusVo userStatusVo){
		userService.userStatus(userStatusVo);
		return ResultVoUtil.success();
	}


	@GetMapping("/queryByUserName")
	public ResultVo<List<UserDto>> queryByUserName(@RequestParam("name") String name){
		return ResultVoUtil.success(userService.queryByUserName(name));
	}

	@GetMapping("/listUser")
	public ResultVo<PageVo<UserVo>> listUser(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit,  String startTime,  String endTime,  String userName){
		PageVo<UserVo> userVoPageVo = userService.listUser(page, limit, startTime, endTime, userName);
		return ResultVoUtil.success(userVoPageVo);
	}

	@GetMapping("/listByRole/{id}")
	public ResultVo<UserInfoShuttleQueryVo> listByRole(@PathVariable String id){
		List<UserInfoVo> userList = new ArrayList<>();
		List<TUser> list = userService.list(new QueryWrapper<>());
		List<String> valueList = userRoleService.list(new QueryWrapper<TUserRole>().eq("role_id", id)).stream().map(userRole -> userRole.getUserId()).collect(Collectors.toList());
		list.forEach(l ->{
			UserInfoVo vo = UserInfoVo.builder().value(l.getUid())
					.title(l.getUserName())
					.disabled(!l.getStatus()).build();
			userList.add(vo);
		});
		return ResultVoUtil.success(UserInfoShuttleQueryVo.builder().shuttleList(userList).valueList(valueList).build());
	}

	@GetMapping("/getUserById")
	public ResultVo<UserEditVo> getUserById(@RequestParam(required = true) String id){
		UserEditVo userEditVo = userService.getUserById(id);
		return ResultVoUtil.success(userEditVo);
	}

	@Log(type = LogEnum.EDIT,option = "修改密码")
	@PostMapping("/editPwd")
	public ResultVo<?> editPwd(@RequestBody @Validated UserChangePwdDto pwdDto) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		userService.editPwd(pwdDto);
		return ResultVoUtil.success();
	}
}
