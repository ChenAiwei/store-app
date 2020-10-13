package com.boot.store.controller.statistics;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.boot.store.dto.statistics.PetSaleDto;
import com.boot.store.dto.statistics.ProductSaleDto;
import com.boot.store.dto.statistics.SaleStatisticsDto;
import com.boot.store.exception.ServiceException;
import com.boot.store.service.statistics.IStatisticsService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author：chenaiwei
 * @Description：StatisticsController
 * @CreateDate：2020/10/12 11:22
 */
@RestController
@RequestMapping("/statics")
public class StatisticsController {
	@Autowired
	private IStatisticsService statisticsService;

	@GetMapping("/dayOfView")
	public ResultVo<SaleStatisticsDto> dayOfView(@RequestParam String date){
		if (StringUtils.isBlank(date)){
			throw new ServiceException("date 不能为空！");
		}
		String dateStart = DateUtil.format(new Date(), "yyyy-MM")+"-01";
		String yearMonth = DateUtil.format(new Date(), "yyyy-MM");
		long between = DateUtil.between(DateUtil.parse(dateStart,"yyyy-MM-dd"), DateUtil.parse(date,"yyyy-MM-dd"), DateUnit.DAY)+1;
		return ResultVoUtil.success(statisticsService.dayOfAllView(yearMonth,(int) between,dateStart,date));
	}

	@GetMapping("/monthOfView")
	public ResultVo<SaleStatisticsDto> monthOfView(@RequestParam String startMonth,@RequestParam String endMonth){
		if (StringUtils.isBlank(startMonth)){
			throw new ServiceException("startMonth 不能为空！");
		}
		if (StringUtils.isBlank(endMonth)){
			throw new ServiceException("endMonth 不能为空！");
		}
		return ResultVoUtil.success(statisticsService.monthOfView(startMonth,endMonth));
	}

	@GetMapping("/productOfView")
	public ResultVo<ProductSaleDto> productOfView(@RequestParam String startDate, @RequestParam String endDate){
		if (StringUtils.isBlank(startDate)){
			throw new ServiceException("startDate 不能为空！");
		}
		if (StringUtils.isBlank(endDate)){
			throw new ServiceException("endDate 不能为空！");
		}
		return ResultVoUtil.success(statisticsService.productOfView(startDate,endDate));
	}

	@GetMapping("/petOfView")
	public ResultVo<PetSaleDto> petOfView(@RequestParam String startDate, @RequestParam String endDate){
		if (StringUtils.isBlank(startDate)){
			throw new ServiceException("startDate 不能为空！");
		}
		if (StringUtils.isBlank(endDate)){
			throw new ServiceException("endDate 不能为空！");
		}
		return ResultVoUtil.success(statisticsService.petOfView(startDate,endDate));
	}
}
