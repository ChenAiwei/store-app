package com.boot.store.service.pet;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.entity.PwPet;
import com.boot.store.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2020-09-17
 */
public interface IPwPetService extends IService<PwPet> {

	/**
	 * 宠物列表
	 * @param page
	 * @param limit
	 * @param typeId
	 * @param name
	 * @return
	 */
	PageVo<PwPet> list(Integer page, Integer limit, String typeId, String name);

	/**
	 * 宠物添加
	 * @param pet
	 */
	void addPet(PwPet pet);

	/**
	 * 宠物编辑
	 * @param pet
	 */
	void editPet(PwPet pet);

	/**
	 * 宠物删除
	 * @param id
	 */
	void deletePet(String id);
}
