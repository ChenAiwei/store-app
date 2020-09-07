package com.boot.store.service.member.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import com.boot.store.vo.member.MemberChargeVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

	@Override
	public Boolean charge(MemberChargeVo memberChargeVo,Double beforeBalance,Double balance) {
		PwMemberMoney memberMoney = new PwMemberMoney();
		memberMoney.setOrderNum(UUIDUtils.genOrder());
		memberMoney.setMemberId(memberChargeVo.getId());
		memberMoney.setBeforeBalance(beforeBalance);
		memberMoney.setBalance(balance);
		memberMoney.setQuota(memberChargeVo.getQuota());
		memberMoney.setActQuota(memberChargeVo.getActQuota());
		memberMoney.setType(StoreConst.MEMBER_MONEY_CHARGE);
		memberMoney.setPayType(memberChargeVo.getPayType());
		memberMoney.setRemark(memberChargeVo.getChargeRemark());
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
}
