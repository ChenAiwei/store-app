package com.boot.store.dto.system;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：UserChangePwdDto
 * @CreateDate：2020/9/24 19:18
 */
@Data
public class UserChangePwdDto implements Serializable {
	@NotNull(message = "userId不能为空！")
	private String userId;
	@NotNull(message = "userName不能为空！")
	private String userName;
	@NotNull(message = "oldPwd不能为空！")
	private String oldPwd;
	@NotNull(message = "newPwd不能为空！")
	private String newPwd;
	@NotNull(message = "confirmPwd不能为空！")
	private String confirmPwd;
}
