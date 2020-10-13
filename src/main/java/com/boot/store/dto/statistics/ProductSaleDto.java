package com.boot.store.dto.statistics;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：ProductSaleDto
 * @CreateDate：2020/10/12 18:42
 */
@Data
public class ProductSaleDto implements Serializable {
	private List<String> xAxisList;
	private List<String> seriesList;
}
