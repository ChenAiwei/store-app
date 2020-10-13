package com.boot.store.dto.statistics;

import lombok.Data;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：SeriesDto
 * @CreateDate：2020/10/12 11:34
 */
@Data
public class SeriesDto implements Serializable {
	private String name;
	private String type;
	private String stack;
	private Boolean smooth;
	private LabelDto label;
	private AreaStyleDto areaStyle;
	private List<String> data;
}
