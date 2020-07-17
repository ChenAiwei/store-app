package com.boot.store.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.dto.system.RoleNameDto;
import com.boot.store.dto.auth.RoleDto;
import com.boot.store.dto.auth.UserRoleInfoDto;
import com.boot.store.entity.TRole;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.role.RoleEditVo;
import com.boot.store.vo.role.RoleVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aiwei
 * @since 2020-04-02
 */
public interface ITRoleService extends IService<TRole> {

	/**
	 * 角色添加
	 * @param role
	 */
	void addRole(RoleDto role);

	/**
	 * 角色编辑
	 * @param role
	 */
	void editRole(RoleDto role);

	/**
	 * 角色删除
	 * @param idLists
	 */
	void delRole(List<String> idLists);

	/**
	 * ID查询角色下的用户
	 * @param uid
	 */
	List<UserRoleInfoDto> getRoleUserList(String uid);

	/**
	 * 角色名查询角色信息
	 * @param name
	 * @return
	 */
	List<RoleDto> queryRoleByName(String name);

	/**\
	 * 角色ID查询角色信息
	 * @param uid
	 * @return
	 */
	RoleEditVo queryRoleById(String uid);

	/**
	 * 用户ID 查询角色名字
	 * @param uidLists
	 * @return
	 */
	Map<String, RoleNameDto> getNameByUserIdList(List<String> uidLists);

	/**
	 * 条件查找角色
	 * @param page
	 * @param limit
	 * @param startTime
	 * @param endTime
	 * @param roleName
	 * @return
	 */
	PageVo<RoleVo> listRole(Integer page, Integer limit, String startTime, String endTime, String roleName);

}
