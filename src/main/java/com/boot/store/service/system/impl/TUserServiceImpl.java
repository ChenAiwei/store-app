package com.boot.store.service.system.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.dto.auth.RoleUserInfoDto;
import com.boot.store.dto.auth.UserDto;
import com.boot.store.dto.system.RoleNameDto;
import com.boot.store.dto.system.UserChangePwdDto;
import com.boot.store.entity.TRole;
import com.boot.store.entity.TUser;
import com.boot.store.entity.TUserRole;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.TUserMapper;
import com.boot.store.service.system.ITUserRoleService;
import com.boot.store.service.system.ITUserService;
import com.boot.store.utils.MD5Util;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.role.RoleNameInfoVo;
import com.boot.store.vo.user.UserEditVo;
import com.boot.store.vo.user.UserStatusVo;
import com.boot.store.vo.user.UserVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
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

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveUser(UserEditVo userVo) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		List<TUser> nameList = this.list(new QueryWrapper<TUser>().eq("user_name", userVo.getUserName()));
		if (CollectionUtils.isNotEmpty(nameList)){
			throw new ServiceException("用户名已存在");
		}
		TUser user = new TUser();
		user.setUid(UUIDUtils.genUid());
		user = combineVo(user,userVo);
		user.setCreateTime(new Date());
		this.save(user);
		this.saveUserRole(user.getUid(),userVo.getRoleIdLits());
	}

	private void saveUserRole(String uid, List<String> roleIdList) {
		userRoleService.remove(new QueryWrapper<TUserRole>().eq("user_id",uid));
		if (CollectionUtils.isEmpty(roleIdList)){
			return;
		}
		List<TUserRole> insertList = new ArrayList<>();
		roleIdList.forEach(roleId ->{
			TUserRole tUserRole = new TUserRole();
			tUserRole.setUid(UUIDUtils.genUid());
			tUserRole.setUserId(uid);
			tUserRole.setRoleId(roleId);
			insertList.add(tUserRole);
		});
		if (CollectionUtils.isNotEmpty(insertList)){
			userRoleService.saveBatch(insertList);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void editUser(UserEditVo userVo) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String uid = userVo.getUid();
		TUser user = this.getById(uid);
		if (null == user){
			throw new ServiceException("修改失败,用户不存在");
		}
		List<TUser> nameList = this.list(new QueryWrapper<TUser>().eq("user_name", userVo.getUserName()));
		if (CollectionUtils.isNotEmpty(nameList) && !nameList.get(0).getUid().equals(userVo.getUid())){
			throw new ServiceException("用户名已存在");
		}
		user = combineVo(user,userVo);
		user.setUpdateTime(new Date());
		this.updateById(user);
		this.saveUserRole(uid,userVo.getRoleIdLits());
	}

	private TUser combineVo(TUser user,UserEditVo userVo) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		user.setUserName(userVo.getUserName());
		user.setNickName(userVo.getNickName());
		user.setMobile(userVo.getPhone());
		user.setEmail(userVo.getEmail());
		user.setPassWord(MD5Util.getEncryptedPwd(userVo.getPassword()));
		user.setBirthday(StringUtils.isNotBlank(userVo.getBirthday())?DateUtil.parseDate(userVo.getBirthday()):null);
		user.setStatus(userVo.getStatus());
		user.setGender(userVo.getSex());
		return user;
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
	public PageVo<UserVo> listUser(Integer page, Integer limit, String startTime, String endTime, String userName,String roleId) {
		if (page == 1){
			page = 0;
		}else{
			page = limit * (page -1);
		}
		List<UserVo> userVoList = this.baseMapper.listUser(page,limit,startTime,endTime,userName,roleId);
		Long count = this.baseMapper.listUserCount(startTime,endTime,userName,roleId);
		return new PageVo<>(count,userVoList);
	}

	@Override
	public UserEditVo getUserById(String id) {
		UserEditVo userEditVo = new UserEditVo();
		TUser user = this.getById(id);
		if (user == null){
			throw new ServiceException("用户不存在!");
		}
		userEditVo.setUid(user.getUid());
		userEditVo.setBirthday(DateUtil.formatDateTime(user.getBirthday()));
		userEditVo.setEmail(user.getEmail());
		userEditVo.setNickName(user.getNickName());
		userEditVo.setPassword("");
		userEditVo.setPhone(user.getMobile());
		userEditVo.setSex(user.getGender());
		userEditVo.setStatus(user.getStatus());
		userEditVo.setUserName(user.getUserName());
		List<RoleNameInfoVo> roleList = new ArrayList<>();
		List<TRole> tRoleList = userRoleService.getRoleService().list(new QueryWrapper<>());
		List<RoleUserInfoDto> roleUserList = this.getRoleUserList(id);
		List<String> roleIdList = roleUserList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
		tRoleList.forEach(role ->{
			RoleNameInfoVo roleNameInfoVo = RoleNameInfoVo.builder().name(role.getRoleName())
					.value(role.getUid())
					.disabled(!role.getStatus())
					.selected(roleIdList.contains(role.getUid()))
					.build();
			roleList.add(roleNameInfoVo);
		});
		userEditVo.setRoleList(roleList);
		return userEditVo;
	}

	@Override
	public void userStatus(UserStatusVo userStatusVo) {
		List<TUser> userList = new ArrayList<>();
		userStatusVo.getUidList().stream().filter(id ->StringUtils.isNotBlank(id)).forEach(id ->{
			userList.add(new TUser().setUid(id).setStatus(userStatusVo.getType() == 0?false:true));
		});
		this.updateBatchById(userList);
	}

	@Override
	public void editPwd(UserChangePwdDto pwdDto) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		TUser user = this.getById(pwdDto.getUserId());
		if (user == null){
			throw new ServiceException("用户不存在!");
		}
		if (!pwdDto.getNewPwd().equals(pwdDto.getConfirmPwd())){
			throw new ServiceException("两次输入密码不一致！");
		}
		boolean isOk = MD5Util.validPassword(pwdDto.getOldPwd(), user.getPassWord());
		if (!isOk){
			throw new ServiceException("旧密码错误！");
		}
		user.setPassWord(MD5Util.getEncryptedPwd(pwdDto.getConfirmPwd()));
		this.updateById(user);
	}

	public List<RoleUserInfoDto> getRoleUserList(String userId){
		List<RoleUserInfoDto> roleUserInfoDtoList = new ArrayList<>();
		List<TUserRole> userRoleList = userRoleService.list(new QueryWrapper<TUserRole>().eq("user_id", userId));
		if (CollectionUtils.isNotEmpty(userRoleList)){
			List<String> roleIdList = userRoleList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
			Collection<TRole> roleCollection = userRoleService.getRoleService().listByIds(roleIdList);
			roleCollection.forEach(role ->{
				roleUserInfoDtoList.add(new RoleUserInfoDto(userId,role.getUid(),role.getRoleName()));
			});
		}
		return roleUserInfoDtoList;
	}
}
