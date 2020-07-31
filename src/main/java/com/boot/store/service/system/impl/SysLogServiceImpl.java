package com.boot.store.service.system.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.SysLog;
import com.boot.store.mapper.SysLogMapper;
import com.boot.store.service.system.ISysLogService;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.system.SysLogQueryParamsVo;
import com.boot.store.vo.system.SysLogVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author aiwei
 * @since 2020-07-13
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

	@Override
	public PageVo<SysLogVo> listCustom(SysLogQueryParamsVo queryParamsVo) {
		Integer page = queryParamsVo.getPage();
		Integer limit = queryParamsVo.getLimit();
		if (queryParamsVo.getPage() == 1){
			queryParamsVo.setPage(0);
		}else{
			queryParamsVo.setPage(limit*(page -1));
			queryParamsVo.setLimit(limit);
		}
		List<SysLogVo> list = this.baseMapper.listCustom(queryParamsVo);
		Long total = this.baseMapper.countCustom(queryParamsVo);
		return new PageVo<SysLogVo>(total,list);
	}
}
