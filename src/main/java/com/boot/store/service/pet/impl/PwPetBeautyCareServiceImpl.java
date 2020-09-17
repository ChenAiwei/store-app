package com.boot.store.service.pet.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.PwPetBeautyCare;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.PwPetBeautyCareMapper;
import com.boot.store.service.pet.IPwPetBeautyCareService;
import com.boot.store.vo.PageVo;
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
public class PwPetBeautyCareServiceImpl extends ServiceImpl<PwPetBeautyCareMapper, PwPetBeautyCare> implements IPwPetBeautyCareService {

	@Override
	public PageVo<PwPetBeautyCare> list(Integer page, Integer limit, String typeId, String name) {
		QueryWrapper<PwPetBeautyCare> queryWrapper = new QueryWrapper<PwPetBeautyCare>().eq("deleted","0");
		if (StringUtils.isNotBlank(typeId)){
			queryWrapper.eq("pet_type",typeId);
		}
		if (StringUtils.isNotBlank(name)){
			queryWrapper.like("project_name",name);
		}
		IPage<PwPetBeautyCare> resultPage = this.page(new Page<>(page,limit),queryWrapper);
		return new PageVo<>(resultPage.getTotal(),resultPage.getRecords());
	}

	@Override
	public void add(PwPetBeautyCare pwPetBeautyCare) {
		this.baseMapper.insert(pwPetBeautyCare);
	}

	@Override
	public void edit(PwPetBeautyCare pwPetBeautyCare) {
		this.baseMapper.updateById(pwPetBeautyCare);
	}

	@Override
	public void delete(Long id) {
		PwPetBeautyCare pwPetBeautyCare = this.baseMapper.selectById(id);
		if (null == pwPetBeautyCare){
			throw new ServiceException("记录不存在!");
		}
		pwPetBeautyCare.setDeleted(1);
		this.baseMapper.updateById(pwPetBeautyCare);
	}
}
