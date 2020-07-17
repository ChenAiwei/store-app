package com.boot.store.vo.role;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：RoleNameInfoVo
 * @CreateDate：2020/7/17 11:23
 */
@Data
@Builder
public class RoleNameInfoVo implements Serializable {
	private String name;
	private String value;
	private Boolean selected;
	private Boolean disabled;
}
