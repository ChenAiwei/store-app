package com.boot.store.controller.member;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.dto.member.PwMemberMoneyDto;
import com.boot.store.entity.PwMember;
import com.boot.store.entity.PwMemberMoney;
import com.boot.store.enums.LogEnum;
import com.boot.store.exception.ServiceException;
import com.boot.store.service.member.IPwMemberMoneyService;
import com.boot.store.service.member.IPwMemberService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import com.boot.store.vo.member.MemberChargeVo;
import com.boot.store.vo.member.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：MemberController
 * @CreateDate：2020/9/1 15:43
 */
@RestController
@RequestMapping("/member")
@Slf4j
public class MemberController {

	@Autowired
	private IPwMemberService memberService;

	@Autowired
	private IPwMemberMoneyService memberMoneyService;

	@GetMapping("/listMember")
	public ResultVo<PageVo<MemberVo>> listMember(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String name, String phone){
		PageVo<MemberVo> memberVoPageVo = memberService.listMember(page, limit, name, phone);
		return ResultVoUtil.success(memberVoPageVo);
	}

	@Log(option = "会员添加",type = LogEnum.ADD)
	@PostMapping("/addMember")
	public ResultVo<?> addMember(@RequestBody @Validated(ValidationGroups.Register.class)MemberVo memberVo){
		checkExit(memberVo.getPhone());
		PwMember member = new PwMember();
		BeanUtils.copyProperties(memberVo,member);
		member.setCreateTime(new Date());
		memberService.save(member);
		return ResultVoUtil.success();
	}

	@Log(option = "会员编辑",type = LogEnum.EDIT)
	@PostMapping("/editMember")
	public ResultVo<?> editMember(@RequestBody @Validated(ValidationGroups.Register.class)MemberVo memberVo){
		PwMember pwMember = memberService.getById(memberVo.getId());
		if (pwMember == null){
			throw new ServiceException("会员不存在！");
		}
		if (!pwMember.getPhone().equals(memberVo.getPhone())){
			throw new ServiceException("会员手机号不允许修改！");
		}
		BeanUtils.copyProperties(memberVo,pwMember);
		pwMember.setId(memberVo.getId());
		memberService.updateById(pwMember);
		return ResultVoUtil.success();
	}

	@GetMapping("/getMember")
	public ResultVo<MemberVo> getMember(@RequestParam("id") String id){
		PwMember pwMember = memberService.getById(id);
		if (pwMember == null){
			throw new ServiceException("会员不存在！");
		}
		MemberVo memberVo = MemberVo.builder().id(pwMember.getId())
				.name(pwMember.getName())
				.phone(pwMember.getPhone())
				.balance(pwMember.getBalance())
				.status(pwMember.getStatus())
				.sex(pwMember.getSex())
				.createTime(DateUtil.formatDateTime(pwMember.getCreateTime()))
				.updateTime(DateUtil.formatDateTime(pwMember.getUpdateTime()))
				.remark(pwMember.getRemark()).build();
		return ResultVoUtil.success(memberVo);
	}

	@GetMapping("/delMember")
	@Log(option = "会员删除",type = LogEnum.DEL)
	public ResultVo<?> delMember(@RequestParam("id") String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("会员不存在！");
		}
		memberService.removeById(Long.valueOf(id));
		return ResultVoUtil.success();
	}

	@Transactional(rollbackFor = Exception.class)
	@Log(option = "会员充值",type = LogEnum.ADD)
	@PostMapping("/charge")
	public ResultVo<?> charge(@RequestBody @Validated MemberChargeVo memberChargeVo){
		PwMember pwMember = memberService.getById(memberChargeVo.getId());
		if (pwMember == null){
			throw new ServiceException("会员不存在！");
		}
		Double beforeBalance = pwMember.getBalance();
		pwMember.setBalance(beforeBalance+Double.valueOf(memberChargeVo.getActQuota()));
		Boolean update = memberService.updateById(pwMember);
		Boolean charge = memberMoneyService.charge(memberChargeVo,beforeBalance,pwMember.getBalance());
		if (update && charge){
			log.info("会员充值,会员：{}，充值：{},余额状态变为：{}->{}",pwMember.getName(),memberChargeVo.getQuota(),beforeBalance,pwMember.getBalance());
			return ResultVoUtil.success("充值成功！");
		}else {
			log.info("会员充值,会员：{}，充值：{},充值失败！",pwMember.getName(),memberChargeVo.getQuota());
			return ResultVoUtil.error("充值失败！");
		}
	}

	@GetMapping("/queryRecord")
	public ResultVo<PageVo<PwMemberMoney>> queryRecord(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String id, String type,String memberName,String startTime,String endTime){
		PageVo<PwMemberMoneyDto> pwMemberMoneyPageVo = memberMoneyService.queryRecord(page, limit, id, type, memberName,startTime, endTime);
		return ResultVoUtil.success(pwMemberMoneyPageVo);
	}

	private void checkExit(String phone) {
		List<PwMember> pwMemberList = memberService.list(new QueryWrapper<PwMember>().eq("phone", phone));
		if (!pwMemberList.isEmpty()){
			throw new ServiceException("会员已存在，请更换手机号注册！");
		}
	}


}
