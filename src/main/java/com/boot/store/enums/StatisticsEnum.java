package com.boot.store.enums;

import lombok.Getter;

/**
 * @Author：chenaiwei
 * @Description：StatisticsEnum
 * @CreateDate：2020/10/12 11:54
 */
@Getter
public enum StatisticsEnum {
	PRODUCT_SALE("P", "商品销售"),
	PET_CARE("J", "宠物寄养"),
	PET_BEAUTY("M", "宠物护理"),
	PET_SALE("S", "宠物售出"),;

	private String type;
	private String name;

	StatisticsEnum(String type, String name) {
		this.type = type;
		this.name = name;
	}
}
