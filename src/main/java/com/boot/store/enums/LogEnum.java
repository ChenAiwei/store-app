package com.boot.store.enums;

import lombok.Getter;

/**
 * @Author：chenaiwei
 * @Description：LogEnum
 * @CreateDate：2020/7/13 9:41
 */
@Getter
public enum LogEnum {
	DEL(0),
	QUERY(1),
	ADD(2),
	EDIT(3);

	private Integer type;

	LogEnum(Integer type) {
		this.type = type;
	}
}
