package com.boot.store.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：PageVo
 * @CreateDate：2020/7/13 18:07
 */
@Data
public class PageVo<T> implements Serializable {
	private Long count;
	private List<T> result;

	public PageVo(Long count, List<T> result) {
		this.count = count;
		this.result = result;
	}
}
