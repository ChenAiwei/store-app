package com.boot.store.dto.statistics;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：ProductSaleDataDto
 * @CreateDate：2020/10/12 18:46
 */
@Data
public class ProductSaleDataDto implements Serializable {
	private String name;
	private Double quota;
}
