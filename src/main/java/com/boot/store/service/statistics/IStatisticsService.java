package com.boot.store.service.statistics;

import com.boot.store.dto.console.ConsoleViewDto;
import com.boot.store.dto.statistics.PetSaleDto;
import com.boot.store.dto.statistics.ProductSaleDto;
import com.boot.store.dto.statistics.SaleStatisticsDto;

import java.util.concurrent.ExecutionException;

/**
 * @Author：chenaiwei
 * @Description：IStatisticsService
 * @CreateDate：2020/10/12 11:41
 */
public interface IStatisticsService {
	/**
	 * 日统计视图
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	SaleStatisticsDto dayOfView(String startDate,String endDate);

	/**
	 * 当前月每一天
	 * @param yearMonth
	 * @param maxMonth
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	SaleStatisticsDto dayOfAllView(String yearMonth,Integer maxMonth,String startDate, String endDate);
	/**
	 * 月统计视图
	 * @param startMonth
	 * @param endMonth
	 * @return
	 */
	SaleStatisticsDto monthOfView(String startMonth, String endMonth);

	/**
	 * 产品销售统计图
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	ProductSaleDto productOfView(String startDate, String endDate);

	/**
	 * 宠物销售统计图
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	PetSaleDto petOfView(String startDate, String endDate);

	/**
	 * 控制台所有视图数据
	 * @return
	 */
	ConsoleViewDto viewAll() throws ExecutionException, InterruptedException;
}
