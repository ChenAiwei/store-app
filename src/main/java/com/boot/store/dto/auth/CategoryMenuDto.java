package com.boot.store.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：CategoryMenuDto
 * @CreateDate：2020/4/3 8:58
 */
@Data
public class CategoryMenuDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 唯一uid
	 */
	@JsonProperty(value = "id")
	private String uid;

	/**
	 * 菜单名称
	 */
	@JsonProperty(value = "title")
	private String name;

	/**
	 * 菜单级别
	 */
	private String tag;

	/**
	 * 简介
	 */
	private String summary;

	/**
	 * 父uid
	 */
	private String parentUid;

	/**
	 * url地址
	 */
	@JsonProperty(value = "href")
	private String url;

	/**
	 * 图标
	 */
	private String icon;

	/**
	 * 排序字段，越大越靠前
	 */
	private Integer sort;

	/**
	 * 状态
	 */
	private Boolean status;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 是否显示 1:是 0:否
	 */
	@JsonProperty(value = "spread")
	private Boolean isShow;

	/**
	 * 菜单类型 0: 目录   1: 菜单  2：按钮
	 */
	private Integer menuType;

	/**
	 * 菜单等级
	 */
	private String menuLevel;

	@JsonProperty(value = "children")
	private List<CategoryMenuDto> childMenuList;

}

