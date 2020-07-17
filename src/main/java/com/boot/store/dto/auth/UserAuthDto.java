package com.boot.store.dto.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：UserAuthDto
 * @CreateDate：2020/4/3 8:51
 */
@Data
public class UserAuthDto implements Serializable {
	private String userId;
	private String userName;
	private String nickName;
	private Integer gender;
	private String avator;
	private String emial;
	private Date birthday;
	private String mobile;
	private Integer loginCount;
	private Date lastLoginTime;
	private String lastLoginIp;
	private Integer userStatus;
	private String uuid;
	private String qqNumber;
	private String weChat;
	private Integer commentStatus;
	private String browser;
	private String os;
	private List<RoleDto> roleList;
	private List<CategoryMenuDto> categoryMenuDtoList;
}
