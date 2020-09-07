package com.boot.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.store.dto.member.PwMemberMoneyDto;
import com.boot.store.entity.PwMemberMoney;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author auto
 * @since 2020-09-01
 */
public interface PwMemberMoneyMapper extends BaseMapper<PwMemberMoney> {

	/**
	 * 账单分页查询
	 * @param page
	 * @param limit
	 * @param id
	 * @param type
	 * @param memberName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<PwMemberMoneyDto> queryRecord(Integer page, Integer limit, String id, String type, String memberName, String startTime, String endTime);

	/**
	 * 账单条数
	 * @param id
	 * @param type
	 * @param memberName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	Long queryRecordCount(String id, String type, String memberName, String startTime, String endTime);
}
