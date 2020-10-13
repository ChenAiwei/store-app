package com.boot.store.dto.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：NormalDto
 * @CreateDate：2020/10/12 11:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NormalDto implements Serializable {
	private Boolean show;
	private String position;
}
