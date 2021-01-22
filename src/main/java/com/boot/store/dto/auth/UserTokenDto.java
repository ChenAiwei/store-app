package com.boot.store.dto.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：UserTokenDto
 * @CreateDate：2020/7/9 17:11
 */
@Data
public class UserTokenDto implements Serializable {
	private String userId;
	private String userName;
	private String avatar;
	private String token;

	public UserTokenDto(String userId, String userName, String avatar, String token) {
		this.userId = userId;
		this.userName = userName;
		this.avatar = avatar;
		this.token = token;
	}
}
