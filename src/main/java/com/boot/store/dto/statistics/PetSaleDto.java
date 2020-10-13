package com.boot.store.dto.statistics;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：PetSaleDto
 * @CreateDate：2020/10/12 18:51
 */
@Data
public class PetSaleDto implements Serializable {
	private List<String> legendList;
	private List<PetSaleTypeDto> data;
}
