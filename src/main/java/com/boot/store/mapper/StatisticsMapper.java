package com.boot.store.mapper;

import com.boot.store.controller.console.SellCountDto;
import com.boot.store.dto.statistics.PetSaleTypeDataDto;
import com.boot.store.dto.statistics.ProductSaleDataDto;
import com.boot.store.dto.statistics.StatisticsDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：StatisticsMapper
 * @CreateDate：2020/10/12 11:50
 */
public interface StatisticsMapper {
	List<StatisticsDto> dayOfView(@Param("startDate") String startDate,@Param("date") String date);

	List<StatisticsDto> monthOfView(@Param("startDate") String startMonth, @Param("date") String endMonth);

	List<ProductSaleDataDto> productOfView(@Param("startDate") String startDate, @Param("endDate") String endDate);

	List<PetSaleTypeDataDto> petOfView(@Param("startDate") String startDate, @Param("endDate") String endDate);

	List<SellCountDto> sellCount(@Param("date") String date);
}
