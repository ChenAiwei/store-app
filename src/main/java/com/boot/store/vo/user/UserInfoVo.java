package com.boot.store.vo.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：UserInfoVo
 * @CreateDate：2020/7/16 15:18
 */
@Data
@Builder
public class UserInfoVo implements Serializable {
	private String value;
	private String title;
	private Boolean disabled;
}
