package com.boot.store.dto.statistics;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：StatisticsDto
 * @CreateDate：2020/10/12 11:53
 */
@Data
public class StatisticsDto implements Serializable {
	private String date;
	private String quota;
	private String type;
}
