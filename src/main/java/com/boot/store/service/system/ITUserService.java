package com.boot.store.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.dto.auth.UserDto;
import com.boot.store.dto.system.UserChangePwdDto;
import com.boot.store.entity.TUser;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.user.UserEditVo;
import com.boot.store.vo.user.UserStatusVo;
import com.boot.store.vo.user.UserVo;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author aiwei
 * @since 2020-04-02
 */
public interface ITUserService extends IService<TUser> {

	/**
	 * 用户添加
	 * @param userVo
	 * @return
	 */
	void saveUser(UserEditVo userVo) throws UnsupportedEncodingException, NoSuchAlgorithmException;

	/**
	 * 用户修改
	 * @param userVo
	 * @return
	 */
	void editUser(UserEditVo userVo) throws UnsupportedEncodingException, NoSuchAlgorithmException;

	/**
	 * 用户ID 查询用户信息
	 * @param uid
	 * @return
	 */
	UserDto queryByUserId(String uid);

	/**
	 * 用户名 查询用户信息
	 * @param name
	 * @return
	 */
	List<UserDto> queryByUserName(String name);

	/**
	 * web分页查询用户信息
	 * @param page
	 * @param limit
	 * @param startTime
	 * @param endTime
	 * @param userName
	 * @return
	 */
	PageVo<UserVo> listUser(Integer page, Integer limit, String startTime, String endTime, String userName);

	/**
	 * web根据id返回编辑的用户信息
	 * @param id
	 * @return
	 */
	UserEditVo getUserById(String id);

	/**
	 * web改变用户状态
	 * @param userStatusVo
	 */
	void userStatus(UserStatusVo userStatusVo);

	/**
	 * 修改用户密码
	 * @param pwdDto
	 */
	void editPwd(UserChangePwdDto pwdDto) throws UnsupportedEncodingException, NoSuchAlgorithmException;
}
