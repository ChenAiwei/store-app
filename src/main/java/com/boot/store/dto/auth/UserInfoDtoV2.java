package com.boot.store.dto.auth;

import lombok.Data;

import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：UserInfoDtoV2
 * @CreateDate：2020/12/23 10:26
 */
@Data
public class UserInfoDtoV2 {
	private String userId;
	private String userName;
	private String address;
	private String email;
	private List<String> menuLevel;

}
