package com.boot.store.dto.commodity;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：CommoditySellDto
 * @CreateDate：2020/9/10 16:52
 */
@Data
public class CommoditySellDto implements Serializable {
	@NotNull(message = "id不允许为空")
	private String id;
	@NotNull(message = "sellCommodityCount不允许为空")
	@Min(1)
	private Integer sellCommodityCount;
	@NotNull(message = "payType不允许为空")
	private Integer payType;
	private String sellRemark;
	@NotNull(message = "vip标识符不允许为空")
	private Integer vip;
	private String memberId;
}
