package com.boot.store.service.commodity.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.dto.commodity.CommodityDto;
import com.boot.store.entity.PwCommodity;
import com.boot.store.mapper.PwCommodityMapper;
import com.boot.store.service.commodity.IPwCommodityService;
import com.boot.store.service.commodity.IPwPurchaseSalesService;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-09-08
 */
@Slf4j
@Service
public class PwCommodityServiceImpl extends ServiceImpl<PwCommodityMapper, PwCommodity> implements IPwCommodityService {

	@Autowired
	private IPwPurchaseSalesService purchaseSalesService;

	@Override
	public PageVo<CommodityDto> list(Integer page, Integer limit, String name, String channelId, String commodityTypeId) {
		if (page == 1){
			page = 0;
		}else{
			page = limit*(page -1);
		}
		List<CommodityDto> dtoList = this.baseMapper.list(page,limit,name,channelId,commodityTypeId);
		Long count = this.baseMapper.count(page,limit,name,channelId,commodityTypeId);
		return new PageVo<>(count,dtoList);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void add(CommodityDto commodityDto) {
		PwCommodity commodity = new PwCommodity();
		BeanUtils.copyProperties(commodityDto,commodity);
		commodity.setCommodityNum(UUIDUtils.genProductId());
		commodity.setCreateTime(new Date());
		this.baseMapper.insert(commodity);
		//添加商品入库记录

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(CommodityDto commodityDto) {
		PwCommodity commodity = new PwCommodity();
		BeanUtils.copyProperties(commodityDto,commodity);
		commodity.setUpdateTime(new Date());
		this.baseMapper.updateById(commodity);
		//商品库存变更记录
	}

	@Override
	public void deleteByIds(List<Long> idsList) {
		List<PwCommodity> pwCommodities = this.baseMapper.selectBatchIds(idsList);
		pwCommodities.forEach(commodity ->{
			commodity.setDeleted(1);
		});
		this.updateBatchById(pwCommodities);
	}

	@Override
	public void up(List<Long> idsList) {
		List<PwCommodity> pwCommodities = this.baseMapper.selectBatchIds(idsList);
		pwCommodities.forEach(commodity ->{
			commodity.setStatus(1);
		});
		this.updateBatchById(pwCommodities);
	}

	@Override
	public void down(List<Long> idsList) {
		List<PwCommodity> pwCommodities = this.baseMapper.selectBatchIds(idsList);
		pwCommodities.forEach(commodity ->{
			commodity.setStatus(0);
		});
		this.updateBatchById(pwCommodities);
	}
}
