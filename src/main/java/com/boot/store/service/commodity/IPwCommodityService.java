package com.boot.store.service.commodity;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.dto.commodity.CommodityDto;
import com.boot.store.dto.commodity.CommoditySellDto;
import com.boot.store.dto.commodity.CommoditySellRecordDto;
import com.boot.store.entity.PwCommodity;
import com.boot.store.vo.PageVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2020-09-08
 */
public interface IPwCommodityService extends IService<PwCommodity> {

	/**
	 * 商品列表
	 * @param page
	 * @param limit
	 * @param name
	 * @param channelId
	 * @param commodityTypeId
	 * @return
	 */
	PageVo<CommodityDto> list(Integer page, Integer limit, String name, String channelId, String commodityTypeId);

	/**
	 * 商品添加
	 * @param commodityDto
	 */
	void add(CommodityDto commodityDto);

	/**
	 * 商品编辑
	 * @param commodityDto
	 */
	void edit(CommodityDto commodityDto);

	/**
	 * 逻辑删除
	 * @param idsList
	 */
	void deleteByIds(List<Long> idsList);

	/**
	 * 商品上架
	 * @param idsList
	 */
	void up(List<Long> idsList);

	/**
	 * 商品下架
	 * @param idsList
	 */
	void down(List<Long> idsList);

	/**
	 * 商品售出
	 * @param commoditySellDto
	 */
	void sell(CommoditySellDto commoditySellDto);

	/**
	 * 获取商品
	 * @param id
	 * @return
	 */
	CommodityDto getCommodity(String id);

	/**
	 * 商品销售记录
	 * @param page
	 * @param limit
	 * @param name
	 * @param commodityNum
	 * @param commodityTypeId
	 * @param channelId
	 * @return
	 */
	PageVo<CommoditySellRecordDto> sellRecord(Integer page, Integer limit, String name, String commodityNum, String commodityTypeId, String channelId,String id);
}
