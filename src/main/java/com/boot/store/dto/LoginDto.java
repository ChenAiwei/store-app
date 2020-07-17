package com.boot.store.dto;

import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：LoginDto
 * @CreateDate：2020/7/9 18:37
 */
@Data
public class LoginDto implements Serializable {
	@NotEmpty(message = "用户名不允许为空")
	@Pattern(regexp = "[\u4E00-\u9FA5A-Za-z0-9]+$",message = "账号只能中文、字母和数字")
	private String username;
	@NotEmpty(message = "密码不允许为空")
	@Size(min=6,max=12,message = "密码长度在6-12之间")
	private String password;
	@NotEmpty(message = "验证码不允许为空")
	private String captcha;
	@NotEmpty(message = "验证码过期")
	private String codeKey;
}
