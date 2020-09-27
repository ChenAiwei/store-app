package com.boot.store.dto.channel;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：StockDto
 * @CreateDate：2020/9/16 9:46
 */
@Data
public class StockDto implements Serializable {
	private String commodityTypeName;
	private String channelName;
	private Integer count;
	private Integer sellCount;
	private Integer stockCount;
	private Double purchasePrice;
	private Double sellPrice;
	private Double profit;
}
