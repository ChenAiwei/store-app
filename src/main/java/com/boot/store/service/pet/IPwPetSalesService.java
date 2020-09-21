package com.boot.store.service.pet;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.entity.PwPetSales;
import com.boot.store.vo.PageVo;

import java.text.ParseException;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2020-09-17
 */
public interface IPwPetSalesService extends IService<PwPetSales> {

	/**
	 * 宠物护理记录
	 * @param id
	 * @param page
	 * @param limit
	 * @param date
	 * @return
	 */
	PageVo<PwPetSales> recordBeauty(Long id, Integer page, Integer limit, String date) throws ParseException;
}
