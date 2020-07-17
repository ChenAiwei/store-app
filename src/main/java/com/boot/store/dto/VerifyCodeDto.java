package com.boot.store.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：VerifyCode
 * @CreateDate：2020/7/8 19:02
 */
@Data
public class VerifyCodeDto implements Serializable {
	private String codeKey;
	private String code;

	public VerifyCodeDto(String codeKey, String code) {
		this.codeKey = codeKey;
		this.code = code;
	}
}
