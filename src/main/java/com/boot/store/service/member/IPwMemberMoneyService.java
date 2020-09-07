package com.boot.store.service.member;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.dto.member.PwMemberMoneyDto;
import com.boot.store.entity.PwMemberMoney;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.member.MemberChargeVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2020-09-01
 */
public interface IPwMemberMoneyService extends IService<PwMemberMoney> {

	/**
	 * 会员充值
	 * @param memberChargeVo
	 * @param beforeBalance
	 * @param balance
	 * @return
	 */
	Boolean charge(MemberChargeVo memberChargeVo,Double beforeBalance,Double balance);

	/**
	 *  账单列表
	 * @param page
	 * @param limit
	 * @param id
	 * @param type
	 * @param memberName
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	PageVo<PwMemberMoneyDto> queryRecord(Integer page, Integer limit, String id, String type,String memberName, String startTime, String endTime);
}
