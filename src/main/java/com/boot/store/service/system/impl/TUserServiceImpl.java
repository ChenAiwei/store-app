package com.boot.store.service.system.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.dto.system.RoleNameDto;
import com.boot.store.dto.auth.RoleUserInfoDto;
import com.boot.store.dto.auth.UserDto;
import com.boot.store.entity.TRole;
import com.boot.store.entity.TUser;
import com.boot.store.entity.TUserRole;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.TUserMapper;
import com.boot.store.service.system.ITRoleService;
import com.boot.store.service.system.ITUserRoleService;
import com.boot.store.service.system.ITUserService;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.user.UserVo;
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
 * 用户表 服务实现类
 * </p>
 *
 * @author aiwei
 * @since 2020-04-02
 */
@Service
public class TUserServiceImpl extends ServiceImpl<TUserMapper, TUser> implements ITUserService {

	@Autowired
	private ITUserRoleService userRoleService;
	@Autowired
	private ITRoleService roleService;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveUser(UserDto userDto) {
		List<TUser> nameList = this.list(new QueryWrapper<TUser>().eq("user_name", userDto.getUserName()));
		if (CollectionUtils.isNotEmpty(nameList)){
			throw new ServiceException("用户名已存在");
		}
		TUser user = new TUser();
		BeanUtils.copyProperties(userDto,user);
		user.setUid(UUIDUtils.genUid());
		user.setCreateTime(new Date());
		this.save(user);
		this.saveUserRole(userDto,user.getUid());
	}

	private void saveUserRole(UserDto userDto, String uid) {
		if (CollectionUtils.isEmpty(userDto.getRoleUserInfoDtoList())){
			return;
		}
		List<String> roleIdList = userDto.getRoleUserInfoDtoList().stream().map(dto -> dto.getRoleId()).collect(Collectors.toList());
		Collection<TRole> roleCollection = roleService.listByIds(roleIdList);
		List<TUserRole> insertList = new ArrayList<>();
		roleCollection.forEach(role ->{
			TUserRole tUserRole = new TUserRole();
			tUserRole.setUid(UUIDUtils.genUid());
			tUserRole.setUserId(uid);
			tUserRole.setRoleId(role.getUid());
			insertList.add(tUserRole);
		});
		if (CollectionUtils.isNotEmpty(insertList)){
			userRoleService.saveBatch(insertList);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void editUser(UserDto userDto) {
		String uid = userDto.getUid();
		TUser user = this.getById(uid);
		if (null == user){
			throw new ServiceException("修改失败,用户不存在");
		}
		List<TUser> nameList = this.list(new QueryWrapper<TUser>().eq("user_name", userDto.getUserName()));
		if (CollectionUtils.isNotEmpty(nameList) && !nameList.get(0).getUid().equals(userDto.getUid())){
			throw new ServiceException("用户名已存在");
		}
		BeanUtils.copyProperties(userDto,user);
		user.setUid(uid);
		this.updateById(user);
		userRoleService.remove(new QueryWrapper<TUserRole>().eq("user_id",uid));
		this.saveUserRole(userDto,uid);
	}

	@Override
	public UserDto queryByUserId(String uid) {
		UserDto userDto = new UserDto();
		TUser user = this.getById(uid);
		if (null != user){
			BeanUtils.copyProperties(this.getById(uid),userDto);
		}
		userDto.setRoleUserInfoDtoList(this.getRoleUserList(uid));
		return userDto;
	}

	@Override
	public List<UserDto> queryByUserName(String name) {
		List<UserDto> resultList = new ArrayList<>();
		List<TUser> userList = this.list(StringUtils.isBlank(name) ? new QueryWrapper<>() : new QueryWrapper<TUser>().eq("user_name", name));
		userList.forEach(user ->{
			UserDto userDto = new UserDto();
			BeanUtils.copyProperties(user,userDto);
			userDto.setRoleUserInfoDtoList(this.getRoleUserList(user.getUid()));
			resultList.add(userDto);
		});
		return resultList;
	}

	@Override
	public PageVo<UserVo> listUser(Integer page, Integer limit, String startTime, String endTime, String userName) {
		List<UserVo> userVoList = new ArrayList<>();
		QueryWrapper<TUser> queryWrapper = new QueryWrapper<TUser>().orderByDesc("create_time");
		if (StringUtils.isNotEmpty(startTime)){
			queryWrapper.ge("create_time",startTime);
		}
		if (StringUtils.isNotEmpty(endTime)){
			queryWrapper.le("create_time",endTime);
		}
		if (StringUtils.isNotEmpty(userName)){
			queryWrapper.eq("user_name",userName);
		}
		IPage<TUser> resultPage = this.page(new Page<>(page,limit),queryWrapper);
		List<String> uidLists = resultPage.getRecords().stream().map(u -> u.getUid()).collect(Collectors.toList());
		Map<String,RoleNameDto> roleNameMap= roleService.getNameByUserIdList(uidLists);
		resultPage.getRecords().forEach(user ->{
			UserVo userVo = UserVo.builder().id(user.getUid())
					.userName(user.getUserName())
					.nickName(user.getNickName())
					.role(roleNameMap.get(user.getUid())!=null?roleNameMap.get(user.getUid()).getRoleName():null)
					.status(user.getStatus()?1:0)
					.email(user.getEmail())
					.mobile(user.getMobile())
					.uuid(user.getUuid())
					.loginCount(user.getLoginCount())
					.lastLoginTime(DateUtil.formatDateTime(user.getLastLoginTime()))
					.lastLoginIp(user.getLastLoginIp())
					.createTime(DateUtil.formatDateTime(user.getCreateTime()))
					.build();
			userVoList.add(userVo);
		});
		return new PageVo<>(resultPage.getTotal(),userVoList);
	}

	public List<RoleUserInfoDto> getRoleUserList(String userId){
		List<RoleUserInfoDto> roleUserInfoDtoList = new ArrayList<>();
		List<TUserRole> userRoleList = userRoleService.list(new QueryWrapper<TUserRole>().eq("user_id", userId));
		if (CollectionUtils.isNotEmpty(userRoleList)){
			List<String> roleIdList = userRoleList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
			Collection<TRole> roleCollection = roleService.listByIds(roleIdList);
			roleCollection.forEach(role ->{
				roleUserInfoDtoList.add(new RoleUserInfoDto(userId,role.getUid(),role.getRoleName()));
			});
		}
		return roleUserInfoDtoList;
	}
}
