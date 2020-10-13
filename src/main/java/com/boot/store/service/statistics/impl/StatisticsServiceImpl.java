package com.boot.store.service.statistics.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.boot.store.controller.console.SellCountDto;
import com.boot.store.dto.console.ConsoleViewDto;
import com.boot.store.dto.statistics.*;
import com.boot.store.enums.StatisticsEnum;
import com.boot.store.mapper.StatisticsMapper;
import com.boot.store.service.statistics.IStatisticsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Author：chenaiwei
 * @Description：StatisticsServiceImpl
 * @CreateDate：2020/10/12 11:42
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {
	private static final Integer DATE_OFF_SET = 6;
	@Autowired
	private StatisticsMapper statisticsMapper;
	@Override
	public SaleStatisticsDto dayOfView(String startDate,String endDate) {
		SaleStatisticsDto resultDto = new SaleStatisticsDto();
		resultDto.setLegend(this.getLegend());
		resultDto.setXAxis(this.getDayXAxis(startDate,endDate));
		List<StatisticsDto> statisticsDtoList = statisticsMapper.dayOfView(startDate,endDate);
		resultDto.setSeries(this.getSeries(statisticsDtoList,resultDto.getLegend(),resultDto.getXAxis()));
		return resultDto;
	}

	@Override
	public SaleStatisticsDto dayOfAllView(String yearMonth,Integer maxMonth,String startDate, String endDate) {
		SaleStatisticsDto resultDto = new SaleStatisticsDto();
		resultDto.setLegend(this.getLegend());
		resultDto.setXAxis(this.getOfAllXAxis(yearMonth,maxMonth));
		List<StatisticsDto> statisticsDtoList = statisticsMapper.dayOfView(startDate,endDate);
		resultDto.setSeries(this.getSeries(statisticsDtoList,resultDto.getLegend(),resultDto.getXAxis()));
		return resultDto;
	}

	private List<String> getOfAllXAxis(String yearMonth, Integer maxMonth) {
		List<String> list = new ArrayList<>();
		for (int i= 1;i<=maxMonth;i++){
			if (i<10){
				list.add(yearMonth+"-0"+i);
			}else{
				list.add(yearMonth+"-"+i);
			}
		}
		return list;
	}

	@Override
	public SaleStatisticsDto monthOfView(String startMonth, String endMonth) {
		//强制显示一年 参数暂时无效
		String year = DateUtil.format(new Date(), "yyyy");
		startMonth = year+"-01";
		endMonth = year+"-12";
		SaleStatisticsDto resultDto = new SaleStatisticsDto();
		resultDto.setLegend(this.getLegend());
		resultDto.setXAxis(this.getMonthXAxis(year,startMonth,endMonth));
		List<StatisticsDto> statisticsDtoList = statisticsMapper.monthOfView(startMonth,endMonth);
		resultDto.setSeries(this.getSeries(statisticsDtoList,resultDto.getLegend(),resultDto.getXAxis()));
		return resultDto;
	}

	@Override
	public ProductSaleDto productOfView(String startDate, String endDate) {
		ProductSaleDto productSaleDto = new ProductSaleDto();
		List<String> xAxisList = new ArrayList<>();
		List<String> seriesList = new ArrayList<>();
		List<ProductSaleDataDto> dataDtoList = statisticsMapper.productOfView(startDate,endDate);
		if (CollectionUtils.isNotEmpty(dataDtoList)){
			dataDtoList.forEach(data ->{
				xAxisList.add(data.getName());
				seriesList.add(data.getQuota().toString());
			});
		}
		productSaleDto.setXAxisList(xAxisList);
		productSaleDto.setSeriesList(seriesList);
		return productSaleDto;
	}

	@Override
	public PetSaleDto petOfView(String startDate, String endDate) {
		PetSaleDto petSaleDto = new PetSaleDto();
		List<String> legendList = new ArrayList<>();
		List<PetSaleTypeDto> data = new ArrayList<>();
		List<PetSaleTypeDataDto> list = statisticsMapper.petOfView(startDate,endDate);
		if (CollectionUtils.isNotEmpty(list)){
			list.forEach(l ->{
				legendList.add(this.getName(l));
				PetSaleTypeDto dto = new PetSaleTypeDto(l.getQuota(),this.getName(l));
				data.add(dto);
			});
		}
		petSaleDto.setLegendList(legendList);
		petSaleDto.setData(data);
		return petSaleDto;
	}

	@Override
	public ConsoleViewDto viewAll() throws ExecutionException, InterruptedException {
		ConsoleViewDto consoleViewDto = new ConsoleViewDto();
		String dateStart = DateUtil.format(new Date(), "yyyy-MM")+"-01";
		String date = DateUtil.format(new Date(), "yyyy-MM-dd");
		String yearMonth = DateUtil.format(new Date(), "yyyy-MM");
		//当天的商品销售  在店的宠物数量 寄养中的宠物数量
		CompletableFuture<List<SellCountDto>> sellCountCompletableFuture = CompletableFuture.supplyAsync(() -> {
			List<SellCountDto> sellCountDtoList = statisticsMapper.sellCount(date);
			return sellCountDtoList;
		});
		//当天的销售利润
		CompletableFuture<SaleStatisticsDto> todaySaleStatisticsDtoCompletableFuture = CompletableFuture.supplyAsync(() -> {
			SaleStatisticsDto saleStatisticsDto = this.dayOfView(date,date);
			return saleStatisticsDto;
		});
		//当月的销售利润
		CompletableFuture<SaleStatisticsDto> saleStatisticsDtoCompletableFuture = CompletableFuture.supplyAsync(() -> {
			long between = DateUtil.between(DateUtil.parse(dateStart,"yyyy-MM-dd"), DateUtil.parse(date,"yyyy-MM-dd"), DateUnit.DAY)+1;
			SaleStatisticsDto saleStatisticsDto = this.dayOfAllView(yearMonth,(int) between,dateStart,date);
			return saleStatisticsDto;
		});
		//当月商品销售
		CompletableFuture<ProductSaleDto> productSaleDtoCompletableFuture = CompletableFuture.supplyAsync(() -> {
			ProductSaleDto productSaleDto = this.productOfView(dateStart, date);
			return productSaleDto;
		});
		///当月宠物利润
		CompletableFuture<PetSaleDto> petSaleDtoCompletableFuture = CompletableFuture.supplyAsync(() -> {
			PetSaleDto petSaleDto = this.petOfView(dateStart, date);
			return petSaleDto;
		});
		Map<String, List<SellCountDto>> collect = sellCountCompletableFuture.get().stream().collect(Collectors.groupingBy(SellCountDto::getType));
		List<SellCountDto> productCountDtoList = collect.get("PRODUCT");
		List<SellCountDto> petCountDtoList = collect.get("PET");
		List<SellCountDto> careCountDtoList = collect.get("CARE");
		if (CollectionUtils.isNotEmpty(productCountDtoList)){
			SellCountDto sellCountDto = productCountDtoList.get(0);
			consoleViewDto.setProductCount(null != sellCountDto ?sellCountDto.getProductCount():"0");
		}
		if (CollectionUtils.isNotEmpty(petCountDtoList)){
			SellCountDto sellCountDto = petCountDtoList.get(0);
			consoleViewDto.setPetCount(null != sellCountDto ?sellCountDto.getProductCount():"0");
		}
		if (CollectionUtils.isNotEmpty(careCountDtoList)){
			SellCountDto sellCountDto = careCountDtoList.get(0);
			consoleViewDto.setFosterCareCount(null != sellCountDto ?sellCountDto.getProductCount():"0");
		}
		SaleStatisticsDto todaysaleStatisticsDto = todaySaleStatisticsDtoCompletableFuture.get();
		List<SeriesDto> series = todaysaleStatisticsDto.getSeries();
		if (CollectionUtils.isNotEmpty(series)){
			SeriesDto seriesDto = series.get(series.size() - 1);
			if (null != seriesDto){
				String money = CollectionUtils.isNotEmpty(seriesDto.getData())? seriesDto.getData().get(0):"0";
				consoleViewDto.setSellMoney(money);
			}else {
				consoleViewDto.setSellMoney("0");
			}
		}else{
			consoleViewDto.setSellMoney("0");
		}
		consoleViewDto.setSaleStatisticsDto(saleStatisticsDtoCompletableFuture.get());
		consoleViewDto.setProductSaleDto(productSaleDtoCompletableFuture.get());
		consoleViewDto.setPetSaleDto(petSaleDtoCompletableFuture.get());
		return consoleViewDto;
	}

	private String getName(PetSaleTypeDataDto l) {
		String name = "";
		StatisticsEnum[] values = StatisticsEnum.values();
		for (StatisticsEnum value : values) {
			if (value.getType().equals(l.getType())){
				name =  value.getName();
				break;
			}
		}
		return name;
	}

	private List<String> getMonthXAxis(String year, String startMonth, String endMonth) {
		List<String> list = new ArrayList<>();
		list.add(startMonth);
		for (int i= 2;i<10;i++){
			list.add(year+"-0"+i);
		}
		list.add(year+"-10");
		list.add(year+"-11");
		list.add(endMonth);
		return list;
	}

	private List<SeriesDto> getSeries(List<StatisticsDto> statisticsDtoList, List<String> legend, List<String> listX) {
		List<SeriesDto> seriesDtoList = new ArrayList<>();
		legend.forEach(l ->{
			SeriesDto dto = new SeriesDto();
			dto.setName(l);
			dto.setType("line");
			dto.setStack("总量");
			dto.setSmooth(true);
			dto.setAreaStyle(new AreaStyleDto());
			List<String> data = new ArrayList<>();
			if ("总利润".equals(l)){
				LabelDto labelDto = new LabelDto();
				NormalDto normalDto = new NormalDto(true,"top");
				labelDto.setNormal(normalDto);
				dto.setLabel(labelDto);
				for (int i = 0; i < listX.size(); i++) {
					int finalI = i;
					AtomicReference<Double> sum = new AtomicReference<>(0d);
					seriesDtoList.forEach(seriesDto -> {
						List<String> list = seriesDto.getData();
						sum.updateAndGet(v -> v + Double.valueOf(list.get(finalI)));
					});
					data.add(sum.get().toString());
				}
			}else{
				List<StatisticsDto> collect = statisticsDtoList.stream().filter(statisticsDto -> statisticsDto.getType().equals(this.getType(l))).collect(Collectors.toList());
				Map<String, List<StatisticsDto>> map = collect.stream().collect(Collectors.groupingBy(StatisticsDto::getDate));
				listX.forEach(x ->{
					data.add(map.get(x) == null?"0":map.get(x).get(0).getQuota());
				});
			}
			dto.setData(data);
			seriesDtoList.add(dto);
		});
		return seriesDtoList;
	}

	private String getType(String l) {
		String type = "";
		StatisticsEnum[] values = StatisticsEnum.values();
		for (StatisticsEnum value : values) {
			if (value.getName().equals(l)){
				type =  value.getType();
				break;
			}
		}
		return type;
	}

	private List<String> getDayXAxis(String startDate, String date) {
		List<String> list = new ArrayList<>();
		if (!startDate.equals(date)){
			DateTime dateTime = DateUtil.parseDate(date);
			for (int i=DATE_OFF_SET;i>=1;i--){
				DateTime offsetDay = DateUtil.offsetDay(dateTime, -i);
				list.add(DateUtil.format(offsetDay, "yyyy-MM-dd"));
			}
		}
		list.add(date);
		return list;
	}


	private List<String> getLegend() {
		List<String> list = new ArrayList<>();
		StatisticsEnum[] values = StatisticsEnum.values();
		for (int i = values.length - 1; i >= 0; i--) {
			list.add(values[i].getName());
		}
		list.add("总利润");
		return list;
	}
}
