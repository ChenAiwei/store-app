package com.boot.store.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：RegisterDto
 * @CreateDate：2020/7/9 16:41
 */
@Data
public class RegisterDto implements Serializable {
	@NotEmpty(message = "用户名不允许为空")
	@Pattern(regexp = "[\u4E00-\u9FA5A-Za-z0-9]+$",message = "账号只能中文、字母和数字")
	private String username;
	@NotEmpty(message = "密码不允许为空")
	@Size(min=6,max=12,message = "密码长度在6-12之间")
	private String password;
}
