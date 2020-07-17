package com.boot.store.controller.system;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.RoleDto;
import com.boot.store.dto.auth.UserRoleInfoDto;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.entity.TRole;
import com.boot.store.entity.TUserRole;
import com.boot.store.enums.LogEnum;
import com.boot.store.service.system.ITCategoryMenuService;
import com.boot.store.service.system.ITRoleService;
import com.boot.store.service.system.ITUserRoleService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import com.boot.store.vo.menu.MenuVo;
import com.boot.store.vo.role.RoleEditVo;
import com.boot.store.vo.role.RoleNameInfoVo;
import com.boot.store.vo.role.RoleVo;
import com.boot.store.vo.user.UserInfoShuttleAddVo;
import com.boot.store.vo.user.UserInfoVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：RoleRestController
 * @CreateDate：2020/4/3 8:36
 */
@RestController
@RequestMapping("/role")
@Slf4j
public class RoleController {

	@Autowired
	private ITRoleService roleService;

	@Autowired
	private ITCategoryMenuService categoryMenuService;

	@Autowired
	private ITUserRoleService userRoleService;

	@GetMapping("/queryRoleByName")
	public ResultVo<List<RoleDto>> queryRoleByName(@RequestParam("name") String name){
		return ResultVoUtil.success(roleService.queryRoleByName(name));
	}

	@GetMapping("/getRole/{uid}")
	public ResultVo<RoleEditVo> queryRoleById(@PathVariable String uid){
		return ResultVoUtil.success(roleService.queryRoleById(uid));
	}

	@Log(option = "添加角色",type = LogEnum.ADD)
	@PostMapping("/addRole")
	public ResultVo<?> addRole(@Validated(ValidationGroups.Register.class) @RequestBody RoleDto role){
		roleService.addRole(role);
		return ResultVoUtil.success();
	}

	@Log(option = "编辑角色",type = LogEnum.EDIT)
	@PostMapping("/editRole")
	public ResultVo<?> editRole(@Validated(ValidationGroups.Editer.class) @RequestBody RoleDto role) {
		roleService.editRole(role);
		return ResultVoUtil.success();
	}

	@Log(option = "删除角色",type = LogEnum.DEL)
	@PostMapping("/deleteRole")
	public ResultVo<?> delRole(@RequestBody List<String> idLists){
		roleService.delRole(idLists);
		return ResultVoUtil.success();
	}

	@GetMapping("/getRoleUserList/{uid}")
	public ResultVo<List<UserRoleInfoDto>> getRoleUserList(@PathVariable String uid){
		return ResultVoUtil.success(roleService.getRoleUserList(uid));
	}

	@GetMapping("/listRole")
	public ResultVo<List<RoleVo>> listRole(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String startTime, String endTime, String roleName){
		PageVo<RoleVo> roleVoPageVo = roleService.listRole(page, limit, startTime, endTime, roleName);
		return ResultVoUtil.success(roleVoPageVo);
	}

	@Log(option = "角色添加用户",type = LogEnum.ADD)
	@PostMapping("/addUser")
	@Transactional(rollbackFor = Exception.class)
	public ResultVo<?> addUser(@RequestBody @Validated  UserInfoShuttleAddVo vo){
		userRoleService.remove(new QueryWrapper<TUserRole>().eq("role_id",vo.getId()));
		List<UserInfoVo> voIdList = vo.getIdList();
		if (CollectionUtils.isNotEmpty(voIdList)){
			List<TUserRole> userRoleList = new ArrayList<>();
			voIdList.forEach(vol ->{
				TUserRole userRole = new TUserRole();
				userRole.setUid(UUIDUtils.genUid());
				userRole.setRoleId(vo.getId());
				userRole.setUserId(vol.getValue());
				userRoleList.add(userRole);
			});
			userRoleService.saveBatch(userRoleList);
		}
		return ResultVoUtil.success();
	}

	@GetMapping("/menuTree")
	public ResultVo<List<MenuVo>> menuTree(){
		return ResultVoUtil.success(categoryMenuService.menuTree(null));
	}

	@GetMapping("/roleNameInfo")
	public ResultVo<List<RoleNameInfoVo>> roleNameInfo(){
		List<RoleNameInfoVo> resultList = new ArrayList<>();
		List<TRole> list = roleService.list(new QueryWrapper<>());
		list.forEach(l->{
			RoleNameInfoVo roleNameInfoVo = RoleNameInfoVo.builder().value(l.getUid())
					.name(l.getRoleName())
					.selected(false)
					.disabled(!l.getStatus()).build();
			resultList.add(roleNameInfoVo);
		});
		return ResultVoUtil.success(resultList);
	}
}
