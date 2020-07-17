package com.boot.store.dto.system;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：RoleNameDto
 * @CreateDate：2020/7/13 19:16
 */
@Data
public class RoleNameDto implements Serializable {
	private String userId;
	private String roleName;
}
