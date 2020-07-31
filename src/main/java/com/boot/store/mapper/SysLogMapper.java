package com.boot.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.store.entity.SysLog;
import com.boot.store.vo.system.SysLogQueryParamsVo;
import com.boot.store.vo.system.SysLogVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author aiwei
 * @since 2020-07-13
 */
public interface SysLogMapper extends BaseMapper<SysLog> {

	/**
	 * web 系统日志查询
	 * @param queryParamsVo
	 * @return
	 */
	List<SysLogVo> listCustom(@Param("queryParamsVo") SysLogQueryParamsVo queryParamsVo);

	/**
	 * web 系统日志查询 总数
	 * @param queryParamsVo
	 * @return
	 */
	Long countCustom(@Param("queryParamsVo") SysLogQueryParamsVo queryParamsVo);
}
