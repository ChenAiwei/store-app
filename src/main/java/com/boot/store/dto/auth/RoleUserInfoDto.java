package com.boot.store.dto.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：RoleUserInfoDto
 * @CreateDate：2020/7/6 11:16
 */
@Data
public class RoleUserInfoDto implements Serializable {
	private String userId;
	private String roleId;
	private String roleName;

	public RoleUserInfoDto(String userId, String roleId, String roleName) {
		this.userId = userId;
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public RoleUserInfoDto() {
	}
}
