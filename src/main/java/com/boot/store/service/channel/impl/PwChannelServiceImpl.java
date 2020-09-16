package com.boot.store.service.channel.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.dto.channel.StockDto;
import com.boot.store.entity.PwChannel;
import com.boot.store.mapper.PwChannelMapper;
import com.boot.store.service.channel.IPwChannelService;
import com.boot.store.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-09-08
 */
@Service
public class PwChannelServiceImpl extends ServiceImpl<PwChannelMapper, PwChannel> implements IPwChannelService {

	@Override
	public PageVo<PwChannel> list(Integer page, Integer limit, String name, String type) {
		QueryWrapper<PwChannel> queryWrapper = new QueryWrapper<PwChannel>().eq("deleted",0);
		if (StringUtils.isNotBlank(name)){
			queryWrapper.like("channel_name",name);
		}
		if (StringUtils.isNotBlank(type)){
			queryWrapper.eq("type",type);
		}
		queryWrapper.orderByDesc("create_time");
		IPage<PwChannel> channelIPage = this.page(new Page<>(page,limit),queryWrapper);
		return new PageVo<>(channelIPage.getTotal(),channelIPage.getRecords());
	}

	@Override
	public void deleteById(String id) {
		PwChannel pwChannel = this.baseMapper.selectById(Long.valueOf(id));
		pwChannel.setDeleted(1);
		this.baseMapper.updateById(pwChannel);
	}

	@Override
	public PageVo<StockDto> stockList(Integer page, Integer limit, String commodityType, String channelType) {
		if (page == 1){
			page = 0;
		}else{
			page = limit*(page -1);
		}
		List<StockDto> stockList = this.baseMapper.stockList(page,limit,commodityType,channelType);
		Long stockCount =  this.baseMapper.stockCount(page,limit,commodityType,channelType);
		return new PageVo<>(stockCount,stockList);
	}
}
