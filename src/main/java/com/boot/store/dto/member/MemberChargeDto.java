package com.boot.store.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：MemberChargeVo
 * @CreateDate：2020/9/1 18:55
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberChargeDto implements Serializable {
	private Long id;
	/**
	 * 额度
	 */
	@NotNull(message = "充值额度不能为空")
	private Double quota;
	/**
	 * 实际付款额度
	 */
	@NotNull(message = "实际付款额度不能为空")
	private Double actQuota;
	/**
	 * 模式 1微信 2支付宝 3现金 4其他
	 */
	@NotNull(message = "支付模式不能为空")
	private Integer payType;
	/**
	 * 备注
	 */
	private String chargeRemark;
}
