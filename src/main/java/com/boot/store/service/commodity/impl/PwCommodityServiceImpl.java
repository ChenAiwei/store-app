package com.boot.store.service.commodity.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.consts.StoreConst;
import com.boot.store.context.UserContextHolder;
import com.boot.store.dto.commodity.CommodityDto;
import com.boot.store.dto.commodity.CommoditySellDto;
import com.boot.store.entity.PwChannel;
import com.boot.store.entity.PwCommodity;
import com.boot.store.entity.PwPurchaseSales;
import com.boot.store.entity.PwStockRecord;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.PwCommodityMapper;
import com.boot.store.service.channel.IPwChannelService;
import com.boot.store.service.commodity.IPwCommodityService;
import com.boot.store.service.commodity.IPwPurchaseSalesService;
import com.boot.store.service.commodity.IPwStockRecordService;
import com.boot.store.service.member.IPwMemberService;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
	@Autowired
	private IPwStockRecordService stockRecordService;
	@Autowired
	private IPwChannelService channelService;

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
		PwStockRecord stockRecord = new PwStockRecord();
		stockRecord.setCommodityId(commodity.getId());
		stockRecord.setType(StoreConst.STOCK_ADD);
		stockRecord.setAfterCount(commodity.getCount());
		stockRecord.setAfterPrice(commodity.getPurchasePrice());
		stockRecord.setAfterMoney(commodity.getPurchasePrice().multiply(new BigDecimal(commodity.getCount())));
		stockRecord.setOptId(UserContextHolder.currentUser().getUid());
		stockRecord.setCreateTime(new Date());
		stockRecordService.save(stockRecord);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void edit(CommodityDto commodityDto) {
		PwCommodity beforeCommodity = this.baseMapper.selectById(commodityDto.getId());
		PwCommodity commodity = new PwCommodity();
		BeanUtils.copyProperties(commodityDto,commodity);
		commodity.setUpdateTime(new Date());
		this.baseMapper.updateById(commodity);
		//商品库存变更记录
		Integer beforeCount = beforeCommodity.getCount();
		Integer afterCount = commodity.getCount();
		BigDecimal beforePurchasePrice = beforeCommodity.getPurchasePrice();
		BigDecimal afterPurchasePrice = commodity.getPurchasePrice();
		if (beforeCount.equals(afterCount) && beforePurchasePrice.compareTo(afterPurchasePrice) == 0){
			log.info("产品：{}，库存数量和进价没更新，数量：{}，进价：{}",commodity.getName(),commodity.getCount(),commodity.getPurchasePrice());
			return;
		}
		BigDecimal beforeMoney = beforeCommodity.getPurchasePrice().multiply(new BigDecimal(beforeCommodity.getCount()));
		BigDecimal afterMoney = commodity.getPurchasePrice().multiply(new BigDecimal(commodity.getCount()));
		PwStockRecord stockRecord = new PwStockRecord();
		stockRecord.setCommodityId(commodity.getId());
		if (afterCount > beforeCount){
			stockRecord.setType(StoreConst.STOCK_ADD);
		}else {
			stockRecord.setType(StoreConst.STOCK_SUB);
		}
		stockRecord.setBeforeCount(beforeCount);
		stockRecord.setAfterCount(afterCount);
		stockRecord.setBeforePrice(beforePurchasePrice);
		stockRecord.setAfterPrice(afterPurchasePrice);
		stockRecord.setBeforeMoney(beforeMoney);
		stockRecord.setAfterMoney(afterMoney);
		stockRecord.setOptId(UserContextHolder.currentUser().getUid());
		stockRecord.setCreateTime(new Date());
		stockRecordService.save(stockRecord);
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

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void sell(CommoditySellDto commoditySellDto) {
		PwCommodity commodity = this.baseMapper.selectById(commoditySellDto.getId());
		if (null == commodity){
			throw new ServiceException("商品不存在！");
		}
		if (commoditySellDto.getVip() == 1 && StringUtils.isBlank(commoditySellDto.getMemberId())){
			throw new ServiceException("会员不存在！");
		}
		PwChannel channel = channelService.getById(commoditySellDto.getChannelId());
		if (null == channel){
			throw new ServiceException("渠道不存在！");
		}
		//已经售出的数量
		AtomicReference<Integer> sellCount = new AtomicReference<>(0);
		List<PwPurchaseSales> sellList = purchaseSalesService.list(new QueryWrapper<PwPurchaseSales>().eq("commodity_id", commodity.getId()));
		sellList.forEach(sell ->{
			sellCount.updateAndGet(v -> v + sell.getCount());
		});
		//剩余数量
		Integer stockCount = commodity.getCount() - sellCount.get();
		if (stockCount < commoditySellDto.getSellCommodityCount()){
			throw new ServiceException("售出失败，商品库存不足！");
		}
		BigDecimal quota = new BigDecimal(0);
		if(commodity.getDiscount() == 1){
			//打折产品
			quota = commodity.getPurchasePrice().multiply(new BigDecimal(commodity.getDiscount()));
		}else{
			quota = commodity.getPurchasePrice();
		}
		//总扣费
		BigDecimal money = quota.multiply(new BigDecimal(commoditySellDto.getSellCommodityCount()));
		//生成订单
		PwPurchaseSales purchaseSales = new PwPurchaseSales();
		purchaseSales.setOrderNum(UUIDUtils.genOrder());
		purchaseSales.setCommodityId(commodity.getId());
		purchaseSales.setCount(commoditySellDto.getSellCommodityCount());
		purchaseSales.setChannelId(Long.valueOf(commoditySellDto.getChannelId()));
		if (commoditySellDto.getVip() == 1){
			purchaseSales.setMemberId(Long.valueOf(commoditySellDto.getMemberId()));
			purchaseSales.setConsumerType(1);
		}else {
			purchaseSales.setConsumerType(2);
		}
		purchaseSales.setQuota(quota);
		purchaseSales.setCreateTime(new Date());
		purchaseSales.setRemark(commoditySellDto.getSellRemark());

	}
}
