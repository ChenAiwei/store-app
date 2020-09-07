package com.boot.store.dto.member;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author auto
 * @since 2020-09-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pw_member_money")
public class PwMemberMoneyDto extends Model<PwMemberMoneyDto> {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 订单号
     */
    private String orderNum;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 余额
     */
    private Double balance;

    /**
     * 上一次余额
     */
    private Double beforeBalance;

    /**
     * 类型 1充值 2消费
     */
    private Integer type;

    /**
     * 额度
     */
    private Double quota;

    /**
     * 实际付款额度
     */
    private Double actQuota;

    /**
     * 模式 1微信 2支付宝 3现金 4其他
     */
    private Integer payType;

    /**
     * 备注
     */
    private String remark;

    private Date createTime;

    private String memberName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
