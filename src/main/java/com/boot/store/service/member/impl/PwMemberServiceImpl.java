package com.boot.store.service.member.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.PwMember;
import com.boot.store.entity.PwMemberMoney;
import com.boot.store.mapper.PwMemberMapper;
import com.boot.store.service.member.IPwMemberMoneyService;
import com.boot.store.service.member.IPwMemberService;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.member.MemberVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class PwMemberServiceImpl extends ServiceImpl<PwMemberMapper, PwMember> implements IPwMemberService {

	@Autowired
	@Lazy
	private IPwMemberMoneyService memberMoneyService;

	@Override
	public PageVo<MemberVo> listMember(Integer page, Integer limit, String name, String phone) {
		List<MemberVo> list = new ArrayList<>();
		QueryWrapper<PwMember> queryWrapper = new QueryWrapper<PwMember>().orderByDesc("update_time");
		if (StringUtils.isNotEmpty(name)){
			queryWrapper.eq("name",name);
		}
		if (StringUtils.isNotEmpty(phone)){
			queryWrapper.eq("phone",phone);
		}
		IPage<PwMember> resultPage = this.page(new Page<>(page,limit),queryWrapper);
		resultPage.getRecords().forEach(record ->{
			List<PwMemberMoney> moneyList = memberMoneyService.list(new QueryWrapper<PwMemberMoney>().eq("member_id", record.getId()));
			MemberVo memberVo = MemberVo.builder().id(record.getId())
					.name(record.getName())
					.phone(record.getPhone())
					.balance(record.getBalance())
					.status(record.getStatus())
					.createTime(DateUtil.formatDateTime(record.getCreateTime()))
					.recordSize(moneyList.size())
					.remark(record.getRemark()).build();
			list.add(memberVo);
		});
		return new PageVo<>(resultPage.getTotal(),list);
	}
}
