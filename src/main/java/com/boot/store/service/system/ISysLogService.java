package com.boot.store.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.entity.SysLog;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.system.SysLogQueryParamsVo;
import com.boot.store.vo.system.SysLogVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author aiwei
 * @since 2020-07-13
 */
public interface ISysLogService extends IService<SysLog> {

	/**
	 * 日志记录查询
	 * @param queryParamsVo
	 * @return
	 */
	PageVo<SysLogVo> listCustom(SysLogQueryParamsVo queryParamsVo);
}
