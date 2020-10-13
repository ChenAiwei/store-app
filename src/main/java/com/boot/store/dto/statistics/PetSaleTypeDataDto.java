package com.boot.store.dto.statistics;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：PetSaleTypeDataDto
 * @CreateDate：2020/10/12 18:55
 */
@Data
public class PetSaleTypeDataDto implements Serializable {
	private String quota;
	private String type;
}
