package com.boot.store.service.commodity.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.consts.StoreConst;
import com.boot.store.context.UserContextHolder;
import com.boot.store.dto.commodity.CommodityDto;
import com.boot.store.dto.commodity.CommoditySellDto;
import com.boot.store.dto.commodity.CommoditySellRecordDto;
import com.boot.store.dto.plugin.XmSelectModelDto;
import com.boot.store.entity.*;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.PwCommodityMapper;
import com.boot.store.service.channel.IPwChannelService;
import com.boot.store.service.commodity.IPwCommodityService;
import com.boot.store.service.commodity.IPwCommodityTypeService;
import com.boot.store.service.commodity.IPwPurchaseSalesService;
import com.boot.store.service.commodity.IPwStockRecordService;
import com.boot.store.service.member.IPwMemberMoneyService;
import com.boot.store.service.member.IPwMemberService;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
	@Autowired
	private IPwStockRecordService stockRecordService;
	@Autowired
	private IPwChannelService channelService;
	@Lazy
	@Autowired
	private IPwMemberMoneyService memberMoneyService;
	@Autowired
	private IPwCommodityTypeService commodityTypeService;
	@Lazy
	@Autowired
	private IPwMemberService memberService;

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
		if (commodity.getStatus() == 0){
			throw new ServiceException("商品已下架！");
		}
		if (commoditySellDto.getVip() == 1 && StringUtils.isBlank(commoditySellDto.getMemberId())){
			throw new ServiceException("会员不存在！");
		}
		PwChannel channel = channelService.getById(commodity.getChannelId());
		if (null == channel || channel.getDeleted() == 1){
			throw new ServiceException("渠道不存在！");
		}
		//已经售出的数量
		Integer sellCount = commodity.getSellCount();
		//剩余数量
		Integer stockCount = commodity.getCount() - sellCount;
		if (stockCount < commoditySellDto.getSellCommodityCount()){
			throw new ServiceException("售出失败，商品库存不足！");
		}
		BigDecimal quota = new BigDecimal(0);
		if(commodity.getDiscount() == 1){
			//打折产品
			quota = commodity.getSellPrice().multiply(commodity.getDiscountRate());
		}else{
			quota = commodity.getSellPrice();
		}
		//减库存
		commodity.setSellCount(commodity.getSellCount()+commoditySellDto.getSellCommodityCount());
		commodity.updateById();
		//总扣费
		BigDecimal money = quota.multiply(new BigDecimal(commoditySellDto.getSellCommodityCount()));
		//生成商品销售订单
		PwPurchaseSales purchaseSales = new PwPurchaseSales();
		purchaseSales.setOrderNum(UUIDUtils.genOrder());
		purchaseSales.setCommodityId(commodity.getId());
		purchaseSales.setCount(commoditySellDto.getSellCommodityCount());
		purchaseSales.setChannelId(commodity.getChannelId());
		if (commoditySellDto.getVip() == 1){
			purchaseSales.setMemberId(Long.valueOf(commoditySellDto.getMemberId()));
			purchaseSales.setConsumerType(1);
		}else {
			purchaseSales.setConsumerType(2);
		}
		purchaseSales.setQuota(quota);
		purchaseSales.setPurchaseQuota(commodity.getPurchasePrice());
		purchaseSales.setCreateTime(new Date());
		purchaseSales.setRemark(commoditySellDto.getSellRemark());
		purchaseSalesService.save(purchaseSales);
		if (commoditySellDto.getVip() == 1){
			//产生会员扣费记录
			memberMoneyService.deduction(Long.valueOf(commoditySellDto.getMemberId()),money,commoditySellDto.getSellRemark(),commoditySellDto.getPayType(),purchaseSales.getOrderNum());
		}
	}

	@Override
	public CommodityDto getCommodity(String id) {
		CommodityDto commodityDto = new CommodityDto();
		PwCommodity commodity = this.getById(Long.valueOf(id));
		BeanUtils.copyProperties(commodity,commodityDto);
		commodityDto.setStockCount(commodityDto.getCount()-commodityDto.getSellCount());
		List<PwCommodityType> typeList = commodityTypeService.list(null);
		List<PwChannel> channelList = channelService.list(new QueryWrapper<PwChannel>().eq("deleted",0));
		List<PwMember> memberList = memberService.list(new QueryWrapper<PwMember>().eq("deleted",0));
		List<XmSelectModelDto> typeSelect = new ArrayList<>();
		List<XmSelectModelDto> channelSelect = new ArrayList<>();
		List<XmSelectModelDto> memberSelect = new ArrayList<>();
		typeList.forEach(type ->{
			typeSelect.add(new XmSelectModelDto(type.getName(),type.getId()));
		});
		channelList.forEach(channel ->{
			channelSelect.add(new XmSelectModelDto(channel.getChannelName(),channel.getId()));
		});
		memberList.forEach(member ->{
			memberSelect.add(new XmSelectModelDto(member.getName(),member.getId()));
		});
		commodityDto.setTypeList(typeSelect);
		commodityDto.setChannelList(channelSelect);
		commodityDto.setMemberList(memberSelect);
		return commodityDto;
	}

	@Override
	public PageVo<CommoditySellRecordDto> sellRecord(Integer page, Integer limit, String orderNum, String name, String commodityNum, String commodityTypeId, String channelId, String id,@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")String date) {
		if (page == 1){
			page = 0;
		}else{
			page = limit*(page -1);
		}
		List<CommoditySellRecordDto> recordList = this.baseMapper.sellRecord(page,limit,orderNum,name,commodityNum,commodityTypeId,channelId,id,date);
		Long recordCount = this.baseMapper.sellRecordCount(name,orderNum,commodityNum,commodityTypeId,channelId,id,date);
		return new PageVo<>(recordCount,recordList);
	}
}
