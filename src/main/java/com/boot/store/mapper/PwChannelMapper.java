package com.boot.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.store.dto.channel.StockDto;
import com.boot.store.entity.PwChannel;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2020-09-08
 */
public interface PwChannelMapper extends BaseMapper<PwChannel> {

	/**
	 * 库存列表
	 * @param page
	 * @param limit
	 * @param commodityType
	 * @param channelType
	 * @return
	 */
	List<StockDto> stockList(Integer page, Integer limit, String commodityType, String channelType);

	/**
	 * 库存列表条数
	 * @param page
	 * @param limit
	 * @param commodityType
	 * @param channelType
	 * @return
	 */
	Long stockCount(Integer page, Integer limit, String commodityType, String channelType);
}
