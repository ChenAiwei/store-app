package com.boot.store.service.pet.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.PwPetSales;
import com.boot.store.mapper.PwPetSalesMapper;
import com.boot.store.service.pet.IPwPetSalesService;
import com.boot.store.vo.PageVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-09-17
 */
@Service
public class PwPetSalesServiceImpl extends ServiceImpl<PwPetSalesMapper, PwPetSales> implements IPwPetSalesService {

	@Override
	public PageVo<PwPetSales> recordBeauty(Long id, Integer page, Integer limit, @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
			String date){
		QueryWrapper<PwPetSales> queryWrapper = new QueryWrapper<PwPetSales>().orderByDesc("create_time");
		if (StringUtils.isNotBlank(date)){
			queryWrapper.apply("DATE_FORMAT(create_time,'%Y-%m-%d') = "+"'"+date+"'");
		}
		queryWrapper.eq("type",2).eq("source",3).eq("source_id",id);
		IPage<PwPetSales> resultPage = this.page(new Page<>(page,limit),queryWrapper);
		return new PageVo<>(resultPage.getTotal(),resultPage.getRecords());
	}
}
