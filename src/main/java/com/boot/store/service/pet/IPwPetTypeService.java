package com.boot.store.service.pet;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.entity.PwPetType;
import com.boot.store.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2020-09-16
 */
public interface IPwPetTypeService extends IService<PwPetType> {

	/**
	 * 宠物类型列表
	 * @param page
	 * @param limit
	 * @param type
	 * @param name
	 * @return
	 */
	PageVo<PwPetType> list(Integer page, Integer limit, String type, String name);

	/**
	 * 逻辑删除
	 * @param id
	 */
	void deleteById(String id);
}
