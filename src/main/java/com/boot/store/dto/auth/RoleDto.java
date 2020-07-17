package com.boot.store.dto.auth;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：RoleDto
 * @CreateDate：2020/4/3 8:55
 */
@Data
public class RoleDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色id
	 */
	@NotBlank(message = "uid 为空",groups = ValidationGroups.Editer.class)
	private String uid;

	/**
	 * 角色名
	 */
	@NotBlank(message = "roleName 为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	private String roleName;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 状态
	 */
	private Boolean status;

	/**
	 * 角色介绍
	 */
	private String summary;

	/**
	 * 角色管辖的菜单的UID
	 */
	private List<String> categoryMenuUids;

	/**
	 * 角色下的用户信息
	 */
	private List<UserRoleInfoDto> userRoleInfoDtoList;
}
