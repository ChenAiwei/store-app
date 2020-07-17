package com.boot.store.dto.auth;

import com.boot.store.entity.TRole;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：UserRoleDto
 * @CreateDate：2020/4/3 9:44
 */
@Data
public class UserRoleDto implements Serializable {
	private String userId;
	private String userName;
	private String nickName;
	private Integer gender;
	private String avator;
	private String emial;
	private Date birthday;
	private String mobile;
	private Integer loginCount;
	private LocalDate lastLoginTime;
	private String lastLoginIp;
	private Boolean userStatus;
	private String uuid;
	private String qqNumber;
	private String weChat;
	private Integer commentStatus;
	private String browser;
	private String os;
	private List<TRole> roleList;
}
