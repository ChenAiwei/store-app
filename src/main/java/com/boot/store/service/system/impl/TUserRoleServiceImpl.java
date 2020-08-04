package com.boot.store.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.TUserRole;
import com.boot.store.mapper.TUserRoleMapper;
import com.boot.store.service.system.ITRoleService;
import com.boot.store.service.system.ITUserRoleService;
import com.boot.store.service.system.ITUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aiwei
 * @since 2020-04-02
 */
@Service
public class TUserRoleServiceImpl extends ServiceImpl<TUserRoleMapper, TUserRole> implements ITUserRoleService {

	@Autowired
	@Lazy
	private ITRoleService roleService;

	@Autowired
	@Lazy
	private ITUserService userService;


	@Override
	public ITUserService getUserService() {
		return userService;
	}

	@Override
	public ITRoleService getRoleService() {
		return roleService;
	}
}
