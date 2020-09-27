package com.boot.store.dto.commodity;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：CommoditySellRecordDto
 * @CreateDate：2020/9/11 15:03
 */
@Data
public class CommoditySellRecordDto implements Serializable {
	private String orderNum;
	private String commodityNum;
	private String name;
	private String commodityTypeName;
	private String channelName;
	private String purchasePrice;
	private String sellPrice;
	private Integer sellCount;
	private String sellMoney;
	private String profit;
	private String customType;
	private Long memberId;
	private String remark;
	private String createTime;
}
