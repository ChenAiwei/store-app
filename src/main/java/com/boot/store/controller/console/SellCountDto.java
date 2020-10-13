package com.boot.store.controller.console;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：SellCountDto
 * @CreateDate：2020/10/13 10:19
 */
@Data
public class SellCountDto implements Serializable {
	private String productCount;
	private String type;
}
