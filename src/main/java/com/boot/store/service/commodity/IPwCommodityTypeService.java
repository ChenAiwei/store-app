package com.boot.store.service.commodity;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.entity.PwCommodityType;
import com.boot.store.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2020-09-08
 */
public interface IPwCommodityTypeService extends IService<PwCommodityType> {

	/**
	 * 列表查询
	 * @param page
	 * @param limit
	 * @param name
	 * @param type
	 * @return
	 */
	PageVo<PwCommodityType> list(Integer page, Integer limit, String name, String type);

	/**
	 * 逻辑删除
	 * @param id
	 */
	void deleteById(Long id);
}
