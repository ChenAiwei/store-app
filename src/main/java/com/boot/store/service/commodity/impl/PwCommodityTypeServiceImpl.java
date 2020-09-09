package com.boot.store.service.commodity.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.PwCommodityType;
import com.boot.store.mapper.PwCommodityTypeMapper;
import com.boot.store.service.commodity.IPwCommodityTypeService;
import com.boot.store.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-09-08
 */
@Service
public class PwCommodityTypeServiceImpl extends ServiceImpl<PwCommodityTypeMapper, PwCommodityType> implements IPwCommodityTypeService {

	@Override
	public PageVo<PwCommodityType> list(Integer page, Integer limit, String name, String type) {
		QueryWrapper<PwCommodityType> queryWrapper = new QueryWrapper<PwCommodityType>().eq("deleted",0).orderByDesc("create_time");
		if (StringUtils.isNotBlank(name)){
			queryWrapper.like("name",name);
		}
		if (StringUtils.isNotBlank(type)){
			queryWrapper.eq("type",type);
		}
		IPage<PwCommodityType> resultPage = this.page(new Page<>(page,limit),queryWrapper);
		return new PageVo<>(resultPage.getTotal(),resultPage.getRecords());
	}

	@Override
	public void deleteById(Long id) {
		PwCommodityType pwCommodityType = this.baseMapper.selectById(id);
		pwCommodityType.setDeleted(1);
		this.baseMapper.updateById(pwCommodityType);
	}
}
