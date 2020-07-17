package com.boot.store.vo.menu;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：MenuVo
 * @CreateDate：2020/7/15 17:05
 */
@Data
@Builder
public class MenuVo implements Serializable {
	private String pid;
	private String id;
	private Integer sort;
	private String title;
	private Boolean spread;
	private Boolean checked;
	private List<MenuVo> children;

}
