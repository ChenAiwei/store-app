package com.boot.store.service.pet.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.PwPet;
import com.boot.store.entity.PwPetType;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.PwPetMapper;
import com.boot.store.service.pet.IPwPetService;
import com.boot.store.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-09-17
 */
@Service
public class PwPetServiceImpl extends ServiceImpl<PwPetMapper, PwPet> implements IPwPetService {

	@Override
	public PageVo<PwPet> list(Integer page, Integer limit, String typeId, String name) {
		QueryWrapper<PwPet> queryWrapper = new QueryWrapper<PwPet>().eq("deleted","0").orderByDesc("create_time");
		if (StringUtils.isNotBlank(typeId)){
			queryWrapper.eq("pet_type_id",typeId);
		}
		if (StringUtils.isNotBlank(name)){
			queryWrapper.like("name",name);
		}
		IPage<PwPet> resultPage = this.page(new Page<>(page,limit),queryWrapper);
		return new PageVo<>(resultPage.getTotal(),resultPage.getRecords());
	}

	@Override
	public void addPet(PwPet pet) {
		pet.setCreateTime(new Date());
		this.baseMapper.insert(pet);
	}

	@Override
	public void editPet(PwPet pet) {
		this.baseMapper.updateById(pet);
	}

	@Override
	public void deletePet(String id) {
		PwPet pwPet = this.baseMapper.selectById(id);
		if (null == pwPet){
			throw new ServiceException("宠物不存在！");
		}
		pwPet.setDeleted(1);
		this.updateById(pwPet);
	}
}
