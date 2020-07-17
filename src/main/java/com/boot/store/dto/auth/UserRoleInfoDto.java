package com.boot.store.dto.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：UserRoleInfoDto
 * @CreateDate：2020/7/6 10:48
 */
@Data
public class UserRoleInfoDto implements Serializable {
	private String roleId;
	private String userId;
	private String userName;

	public UserRoleInfoDto(String roleId, String userId, String userName) {
		this.roleId = roleId;
		this.userId = userId;
		this.userName = userName;
	}

	public UserRoleInfoDto() {}
}
