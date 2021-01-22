package com.boot.store.dto.auth;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：UserTokenDtoV2
 * @CreateDate：2020/12/23 10:24
 */
@Data
public class UserTokenDtoV2 implements Serializable {
	private String token;
	private UserInfoDtoV2 userInfo;

}
