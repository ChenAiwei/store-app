package com.boot.store.service.pet.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.PwCommodityType;
import com.boot.store.entity.PwPetType;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.PwPetTypeMapper;
import com.boot.store.service.pet.IPwPetTypeService;
import com.boot.store.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-09-16
 */
@Service
public class PwPetTypeServiceImpl extends ServiceImpl<PwPetTypeMapper, PwPetType> implements IPwPetTypeService {

	@Override
	public PageVo<PwPetType> list(Integer page, Integer limit, String type, String name) {
		QueryWrapper<PwPetType> queryWrapper = new QueryWrapper<PwPetType>().eq("deleted","0").orderByDesc("create_time");
		if (StringUtils.isNotBlank(type)){
			queryWrapper.eq("pet_type",type);
		}
		if (StringUtils.isNotBlank(name)){
			queryWrapper.eq("pet_name",name);
		}
		IPage<PwPetType> resultPage = this.page(new Page<>(page,limit),queryWrapper);
		return new PageVo<>(resultPage.getTotal(),resultPage.getRecords());
	}

	@Override
	public void deleteById(String id) {
		PwPetType pwPetType = this.baseMapper.selectById(Long.valueOf(id));
		if (null == pwPetType){
			throw new ServiceException("记录不存在！");
		}
		pwPetType.setDeleted(1);
		this.baseMapper.updateById(pwPetType);
	}
}
