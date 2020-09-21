package com.boot.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.store.entity.PwPet;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2020-09-17
 */
public interface PwPetMapper extends BaseMapper<PwPet> {

	/**
	 * 列表查询
	 * @param page
	 * @param limit
	 * @param name
	 * @param typeId
	 * @return
	 */
	List<PwPet> list(Integer page, Integer limit, String name, String typeId);

	/**
	 * 查询总数
	 * @param name
	 * @param typeId
	 * @return
	 */
	Long count(String name, String typeId);
}
