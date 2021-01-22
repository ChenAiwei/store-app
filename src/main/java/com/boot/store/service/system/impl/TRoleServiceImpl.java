package com.boot.store.service.system.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.dto.auth.RoleDto;
import com.boot.store.dto.auth.UserRoleInfoDto;
import com.boot.store.dto.system.RoleNameDto;
import com.boot.store.entity.TRole;
import com.boot.store.entity.TUser;
import com.boot.store.entity.TUserRole;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.TRoleMapper;
import com.boot.store.service.system.ITCategoryMenuService;
import com.boot.store.service.system.ITRoleService;
import com.boot.store.service.system.ITUserRoleService;
import com.boot.store.utils.JsonUtils;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.menu.MenuVo;
import com.boot.store.vo.role.RoleEditVo;
import com.boot.store.vo.role.RoleVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aiwei
 * @since 2020-04-02
 */
@Service
public class TRoleServiceImpl extends ServiceImpl<TRoleMapper, TRole> implements ITRoleService {

	@Autowired
	private ITUserRoleService userRoleService;
	@Autowired
	private ITCategoryMenuService categoryMenuService;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void addRole(RoleDto role) {
		List<TRole> roleList = this.list(new QueryWrapper<TRole>().eq("role_name", role.getRoleName()));
		if (CollectionUtils.isNotEmpty(roleList)){
			throw new ServiceException("角色名已经存在");
		}
		TRole tRole = new TRole();
		tRole.setUid(UUIDUtils.genUid());
		tRole.setRoleName(role.getRoleName());
		tRole.setCreateTime(new Date());
		if (StringUtils.isNotBlank(role.getSummary())){
			tRole.setSummary(role.getSummary());
		}
		tRole.setStatus(role.getStatus());
		if (CollectionUtils.isNotEmpty(role.getCategoryMenuUids())){
			String categoryMenuUidsJstr = JsonUtils.objectToJson(role.getCategoryMenuUids());
			tRole.setCategoryMenuUids(categoryMenuUidsJstr);
		}
		this.save(tRole);
		this.saveUserRole(role,tRole.getUid());
	}

	private void saveUserRole(RoleDto role, String roleId) {
		if (CollectionUtils.isEmpty(role.getUserRoleInfoDtoList())){
			return;
		}
		List<String> userIdList = role.getUserRoleInfoDtoList().stream().map(e -> e.getUserId()).collect(Collectors.toList());
		Collection<TUser> userCollection = userRoleService.getUserService().listByIds(userIdList);
		List<TUserRole> insertList = new ArrayList<>();
		userCollection.forEach(user ->{
			TUserRole tUserRole = new TUserRole();
			tUserRole.setUid(UUIDUtils.genUid());
			tUserRole.setUserId(user.getUid());
			tUserRole.setRoleId(roleId);
			insertList.add(tUserRole);
		});
		if (CollectionUtils.isNotEmpty(insertList)){
			userRoleService.saveBatch(insertList);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void editRole(RoleDto role) {
		TRole tRole = this.getById(role.getUid());
		if (null == tRole){
			throw new ServiceException("修改失败,角色不存在");
		}
		if (!tRole.getRoleName().equals(role.getRoleName())){
			List<TRole> tRoleList = this.list(new QueryWrapper<TRole>().eq("role_name", role.getRoleName()));
			if (CollectionUtils.isNotEmpty(tRoleList)){
				throw new ServiceException("角色名已经存在");
			}
		}
		tRole.setUpdateTime(new Date());
		tRole.setSummary(StringUtils.isNotBlank(role.getSummary())?role.getSummary():null);
		tRole.setStatus(role.getStatus());
		if (CollectionUtils.isNotEmpty(role.getCategoryMenuUids())){
			String categoryMenuUidsJstr = JsonUtils.objectToJson(role.getCategoryMenuUids());
			tRole.setCategoryMenuUids(categoryMenuUidsJstr);
		}else {
			tRole.setCategoryMenuUids("");
		}
		this.updateById(tRole);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delRole(List<String> idLists) {
		if (CollectionUtils.isEmpty(idLists)){
			throw new ServiceException("请选择角色");
		}
		List<String> list = idLists.stream().filter(id -> StringUtils.isNotEmpty(id)).collect(Collectors.toList());
		list.forEach(l->{
			TRole tRole = this.getById(l);
			if (null == tRole){
				throw new ServiceException("角色不存在");
			}
			List<TUserRole> userRoleList = userRoleService.list(new QueryWrapper<TUserRole>().eq("role_id", l));
			if (CollectionUtils.isNotEmpty(userRoleList)){
				throw new ServiceException(tRole.getRoleName()+" 角色下存在用户，请先删除用户");
			}
			this.removeById(l);
		});
	}

	@Override
	public List<UserRoleInfoDto> getRoleUserList(String roleId) {
		List<UserRoleInfoDto> userRoleInfoDtoList = new ArrayList<>();
		List<TUserRole> userRoleList = userRoleService.list(new QueryWrapper<TUserRole>().eq("role_id", roleId));
		if (CollectionUtils.isNotEmpty(userRoleList)){
			List<String> userIdList = userRoleList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
			Collection<TUser> userCollection = userRoleService.getUserService().listByIds(userIdList);
			userCollection.forEach(user ->{
				userRoleInfoDtoList.add(new UserRoleInfoDto(roleId,user.getUid(),user.getUserName()));
			});
		}
		return userRoleInfoDtoList;
	}

	@Override
	public List<RoleDto> queryRoleByName(String name) {
		List<RoleDto> resultList = new ArrayList<>();
		List<TRole> roleList = this.list(StringUtils.isBlank(name) ? new QueryWrapper<>() : new QueryWrapper<TRole>().eq("role_name", name));
		roleList.forEach(role ->{
			RoleDto roleDto = new RoleDto();
			BeanUtils.copyProperties(role,roleDto);
			if (StringUtils.isNotBlank(role.getCategoryMenuUids())){
				roleDto.setCategoryMenuUids(JsonUtils.jsonToList(role.getCategoryMenuUids(),String.class));
			}
			List<UserRoleInfoDto> roleUserList = this.getRoleUserList(role.getUid());
			roleDto.setUserRoleInfoDtoList(roleUserList);
			resultList.add(roleDto);
		});
		return resultList;
	}

	@Override
	public RoleEditVo queryRoleById(String uid) {
		TRole tRole = this.getById(uid);
		if (null != tRole){
			RoleEditVo roleEditVo = RoleEditVo.builder().id(tRole.getUid())
					.name(tRole.getRoleName())
					.status(tRole.getStatus()?1:0)
					.remarks(tRole.getSummary()).build();
			List<String> menuIdList = new ArrayList<>();
			if (StringUtils.isNotBlank(tRole.getCategoryMenuUids())){
				menuIdList = JsonUtils.jsonToList(tRole.getCategoryMenuUids(), String.class);
			}
			List<MenuVo> menuVoList = categoryMenuService.menuTree(menuIdList);
			roleEditVo.setMenuList(menuVoList);
			//List<UserRoleInfoDto> roleUserList = this.getRoleUserList(uid);
			return roleEditVo;
		}else{
			throw new ServiceException("角色不存在!");
		}
	}

	@Override
	public Map<String, RoleNameDto> getNameByUserIdList(List<String> uidLists, String roleId) {
		return this.baseMapper.getNameByUserIdList(uidLists,roleId);
	}

	@Override
	public PageVo<RoleVo> listRole(Integer page, Integer limit, String startTime, String endTime, String roleName) {
		List<RoleVo> roleVoList = new ArrayList<>();
		QueryWrapper<TRole> queryWrapper = new QueryWrapper<TRole>().orderByDesc("create_time");
		if (StringUtils.isNotEmpty(startTime)){
			queryWrapper.ge("create_time",startTime);
		}
		if (StringUtils.isNotEmpty(endTime)){
			queryWrapper.le("create_time",endTime);
		}
		if (StringUtils.isNotEmpty(roleName)){
			queryWrapper.eq("role_name",roleName);
		}
		IPage<TRole> resultPage = this.page(new Page<>(page,limit),queryWrapper);
		resultPage.getRecords().forEach(role ->{
			RoleVo roleVo = RoleVo.builder().id(role.getUid())
					.name(role.getRoleName())
					.remarks(role.getSummary())
					.status(role.getStatus() ? 1 : 0)
					.createTime(DateUtil.formatDateTime(role.getCreateTime())).build();
			roleVoList.add(roleVo);
		});
		return new PageVo<>(resultPage.getTotal(),roleVoList);
	}

}
