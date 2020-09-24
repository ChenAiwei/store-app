package com.boot.store.dto.plugin;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：PictureDataDto
 * @CreateDate：2020/9/24 8:46
 */
@Data
public class PictureDataDto implements Serializable {
	private String alt;
	private Integer pid;
	private String src;
	private String thumb;
}
