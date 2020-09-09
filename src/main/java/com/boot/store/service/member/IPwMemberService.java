package com.boot.store.service.member;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.entity.PwMember;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import com.boot.store.vo.member.MemberVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2020-09-01
 */
public interface IPwMemberService extends IService<PwMember> {

	/**
	 * 会员列表
	 * @param page
	 * @param limit
	 * @param name
	 * @param phone
	 * @return
	 */
	PageVo<MemberVo> listMember(Integer page, Integer limit, String name, String phone);

	/**
	 * 逻辑删除
	 * @param id
	 */
	void deleteById(Long id);
}
