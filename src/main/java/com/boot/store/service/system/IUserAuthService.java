package com.boot.store.service.system;

import com.boot.store.dto.auth.CategoryMenuDto;
import com.boot.store.dto.auth.UserAuthDto;
import com.boot.store.vo.menu.MenuEditVo;

import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：IUserAuthService
 * @CreateDate：2020/4/3 9:05
 */
public interface IUserAuthService {
	/**
	 * uid获取用户的权限信息
	 * @param uid
	 * @return
	 */
	List<UserAuthDto> info(String uid);

	/**
	 * 系统的所有权限Tree结构
	 * @return
	 */
	List<CategoryMenuDto> tree();

	/**
	 * 菜单权限的添加
	 * @param menuEditVo
	 */
	void add(MenuEditVo menuEditVo);

	/**
	 * 菜单权限的获取
	 * @param id
	 * @return
	 */
	MenuEditVo item(String id);

	/**
	 * 菜单权限的编辑
	 * @param menuEditVo
	 */
	void edit(MenuEditVo menuEditVo);

	/**
	 * 删除菜单
	 * @param id
	 * @return
	 */
	Boolean del(String id);
}
