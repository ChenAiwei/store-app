package com.boot.store.dto.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：UserTokenDto
 * @CreateDate：2020/7/9 17:11
 */
@Data
public class UserTokenDto implements Serializable {
	private String userId;
	private String token;

	public UserTokenDto(String userId, String token) {
		this.userId = userId;
		this.token = token;
	}
}
