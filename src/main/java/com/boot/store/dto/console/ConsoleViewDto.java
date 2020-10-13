package com.boot.store.dto.console;

import com.boot.store.dto.statistics.PetSaleDto;
import com.boot.store.dto.statistics.ProductSaleDto;
import com.boot.store.dto.statistics.SaleStatisticsDto;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：ConsoleViewDto
 * @CreateDate：2020/10/13 10:00
 */
@Data
public class ConsoleViewDto implements Serializable {
	/**
	 * 收入
	 */
	private String sellMoney;
	/**
	 * 商品
	 */
	private String productCount;
	/**
	 * 在店宠物
	 */
	private String petCount;
	/**
	 * 寄养中宠物
	 */
	private String fosterCareCount;
	/**
	 * 商品销售图
	 */
	private ProductSaleDto productSaleDto;
	/**
	 * 宠物利润分布图
	 */
	private PetSaleDto petSaleDto;
	/**
	 * 利润趋势图
	 */
	private SaleStatisticsDto saleStatisticsDto;




}
