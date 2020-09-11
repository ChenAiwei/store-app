package com.boot.store.service.member.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.consts.StoreConst;
import com.boot.store.dto.member.PwMemberMoneyDto;
import com.boot.store.entity.PwMember;
import com.boot.store.entity.PwMemberMoney;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.PwMemberMoneyMapper;
import com.boot.store.service.member.IPwMemberMoneyService;
import com.boot.store.service.member.IPwMemberService;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import com.boot.store.dto.member.MemberChargeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-09-01
 */
@Service
public class PwMemberMoneyServiceImpl extends ServiceImpl<PwMemberMoneyMapper, PwMemberMoney> implements IPwMemberMoneyService {

	@Lazy
	@Autowired
	private IPwMemberService memberService;

	@Override
	public Boolean charge(MemberChargeDto memberChargeDto, BigDecimal beforeBalance, BigDecimal balance) {
		PwMemberMoney memberMoney = new PwMemberMoney();
		memberMoney.setOrderNum(UUIDUtils.genOrder());
		memberMoney.setMemberId(memberChargeDto.getId());
		memberMoney.setBeforeBalance(beforeBalance);
		memberMoney.setBalance(balance);
		memberMoney.setQuota(new BigDecimal(memberChargeDto.getQuota()));
		memberMoney.setActQuota(new BigDecimal(memberChargeDto.getActQuota()));
		memberMoney.setType(StoreConst.MEMBER_MONEY_CHARGE);
		memberMoney.setPayType(memberChargeDto.getPayType());
		memberMoney.setRemark(memberChargeDto.getChargeRemark());
		memberMoney.setCreateTime(new Date());
		return this.save(memberMoney);
	}

	@Override
	public PageVo<PwMemberMoneyDto> queryRecord(Integer page, Integer limit, String id, String type, String memberName,String startTime, String endTime) {
		if (page == 1){
			page = 0;
		}else{
			page = limit*(page -1);
		}
		List<PwMemberMoneyDto> resultList = this.baseMapper.queryRecord(page,limit,id,type,memberName,startTime,endTime);
		Long total = this.baseMapper.queryRecordCount(id,type,memberName,startTime,endTime);
		return new PageVo<>(total,resultList);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deduction(Long id, BigDecimal money, String sellRemark, Integer payType, String orderNum) {
		PwMember member = memberService.getById(id);
		if (null == member || member.getDeleted() == 1){
			throw new ServiceException("会员不存在！");
		}
		if (member.getStatus() == 0){
			throw new ServiceException("会员已过期！");
		}
		if (null == member.getBalance()){
			throw new ServiceException("会员余额不足，请充值！");
		}
		BigDecimal beforeBalance = member.getBalance();
		BigDecimal afterBalance = beforeBalance.subtract(money);
		if (afterBalance.compareTo(new BigDecimal(0)) == -1){
			throw new ServiceException("会员余额不足，请充值！");
		}
		member.setBalance(afterBalance);
		member.setUpdateTime(new Date());
		memberService.updateById(member);

		PwMemberMoney memberMoney = new PwMemberMoney();
		memberMoney.setOrderNum(orderNum);
		memberMoney.setMemberId(id);
		memberMoney.setBeforeBalance(beforeBalance);
		memberMoney.setBalance(afterBalance);
		memberMoney.setQuota(money);
		memberMoney.setActQuota(money);
		memberMoney.setType(StoreConst.MEMBER_MONEY_CONSUME);
		memberMoney.setPayType(payType);
		memberMoney.setRemark(sellRemark);
		memberMoney.setCreateTime(new Date());
		this.save(memberMoney);
	}
}
