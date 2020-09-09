package com.boot.store.service.channel;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.entity.PwChannel;
import com.boot.store.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2020-09-08
 */
public interface IPwChannelService extends IService<PwChannel> {

	/**
	 * 渠道商列表
	 * @param page
	 * @param limit
	 * @param name
	 * @param type
	 * @return
	 */
	PageVo<PwChannel> list(Integer page, Integer limit, String name, String type);

	/**
	 * 逻辑删除
	 * @param id
	 */
	void deleteById(String id);
}
