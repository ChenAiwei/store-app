package com.boot.store.dto.statistics;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：PetSaleTypeDto
 * @CreateDate：2020/10/12 18:52
 */
@Data
public class PetSaleTypeDto implements Serializable {
	private String value;
	private String name;

	public PetSaleTypeDto(String value, String name) {
		this.value = value;
		this.name = name;
	}
}
