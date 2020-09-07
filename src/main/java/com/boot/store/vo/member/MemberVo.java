package com.boot.store.vo.member;

import com.boot.store.dto.auth.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：MemberVo
 * @CreateDate：2020/9/1 15:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberVo implements Serializable {

	@NotNull(message = "ID不能为空",groups = ValidationGroups.Editer.class)
	private Long id;

	/**
	 * 会员名
	 */
	@NotNull(message = "会员名不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private String name;

	/**
	 * 性别
	 */
	private Integer sex;

	/**
	 * 手机号码
	 */
	@NotNull(message = "手机号不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private String phone;

	/**
	 * 余额
	 */
	private Double balance;

	/**
	 * 0 过期 1正常
	 */
	private Integer status;

	private String createTime;

	private String updateTime;

	/**
	 * 备注
	 */
	private String remark;
	private Integer recordSize;
}
