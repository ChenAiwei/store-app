package com.boot.store.service.pet;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.entity.PwPetBeautyCare;
import com.boot.store.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2020-09-17
 */
public interface IPwPetBeautyCareService extends IService<PwPetBeautyCare> {

	/**
	 * 服务项目列表
	 * @param page
	 * @param limit
	 * @param typeId
	 * @param name
	 * @return
	 */
	PageVo<PwPetBeautyCare> list(Integer page, Integer limit, String typeId, String name);

	/**
	 * 宠物服务添加
	 * @param pwPetBeautyCare
	 */
	void add(PwPetBeautyCare pwPetBeautyCare);

	/**
	 * 宠物服务编辑
	 * @param pwPetBeautyCare
	 */
	void edit(PwPetBeautyCare pwPetBeautyCare);

	/**
	 * 宠物服务删除
	 * @param id
	 */
	void delete(Long id);
}
