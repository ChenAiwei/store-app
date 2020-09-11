package com.boot.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.store.dto.commodity.CommodityDto;
import com.boot.store.dto.commodity.CommoditySellRecordDto;
import com.boot.store.entity.PwCommodity;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2020-09-08
 */
public interface PwCommodityMapper extends BaseMapper<PwCommodity> {

	/**
	 * 商品列表
	 * @param page
	 * @param limit
	 * @param name
	 * @param channelId
	 * @param commodityTypeId
	 * @return
	 */
	List<CommodityDto> list(Integer page, Integer limit, String name, String channelId, String commodityTypeId);

	/**
	 * 商品列表总数
	 * @param page
	 * @param limit
	 * @param name
	 * @param channelId
	 * @param commodityTypeId
	 * @return
	 */
	Long count(Integer page, Integer limit, String name, String channelId, String commodityTypeId);

	/**
	 * 销售记录
	 * @param page
	 * @param limit
	 * @param name
	 * @param commodityNum
	 * @param commodityTypeId
	 * @param commodityNumChannelId
	 * @return
	 */
	List<CommoditySellRecordDto> sellRecord(Integer page, Integer limit, String name, String commodityNum, String commodityTypeId, String commodityNumChannelId,String id);

	/**
	 * 销售记录count
	 * @param name
	 * @param commodityNum
	 * @param commodityTypeId
	 * @param commodityNumChannelId
	 * @return
	 */
	Long sellRecordCount(String name, String commodityNum, String commodityTypeId, String commodityNumChannelId,String id);
}
