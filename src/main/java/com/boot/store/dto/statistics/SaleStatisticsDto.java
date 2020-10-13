package com.boot.store.dto.statistics;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：SaleStatisticsDto
 * @CreateDate：2020/10/12 11:24
 */
@Data
public class SaleStatisticsDto implements Serializable {
	/**
	 * 类型
	 */
	private List<String> legend;
	/**
	 * X轴字段
	 */
	private List<String> xAxis;

	private List<SeriesDto> series;
}
