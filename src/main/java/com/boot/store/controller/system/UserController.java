package com.boot.store.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.store.dto.auth.UserDto;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.entity.TUser;
import com.boot.store.entity.TUserRole;
import com.boot.store.service.system.ITUserRoleService;
import com.boot.store.service.system.ITUserService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import com.boot.store.vo.user.UserInfoShuttleQueryVo;
import com.boot.store.vo.user.UserInfoVo;
import com.boot.store.vo.user.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

	@GetMapping("/delUser/{uid}")
	@Transactional(rollbackFor = Exception.class)
	public ResultVo<?> delUser(@PathVariable String uid){
		userRoleService.remove(new QueryWrapper<TUserRole>().eq("user_id",uid));
		userService.removeById(uid);
		return ResultVoUtil.success();
	}

	@PostMapping("/addUser")
	public ResultVo<?> addUser(@Validated(ValidationGroups.Register.class) @RequestBody UserDto userDto){
		userService.saveUser(userDto);
		return ResultVoUtil.success();
	}

	@PostMapping("/editUser")
	public ResultVo<String> editUser(@Validated(ValidationGroups.Editer.class) @RequestBody UserDto userDto){
		userService.editUser(userDto);
		return ResultVoUtil.success();
	}


	@GetMapping("/queryByUserName")
	public ResultVo<List<UserDto>> queryByUserName(@RequestParam("name") String name){
		return ResultVoUtil.success(userService.queryByUserName(name));
	}

	@GetMapping("/listUser")
	public ResultVo<List<UserVo>> listUser(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit,  String startTime,  String endTime,  String userName){
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
}
