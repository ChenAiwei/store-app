package com.boot.store.vo.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：UserVo
 * @CreateDate：2020/7/13 15:44
 */
@Data
@Builder
public class UserVo implements Serializable {
	private String id;
	private String userName;
	private String nickName;
	private String role;
	private Integer status;
	private String email;
	private String mobile;
	private String uuid;
	private Integer loginCount;
	private String lastLoginTime;
	private String lastLoginIp;
	private String createTime;
}
