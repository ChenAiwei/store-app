package com.boot.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class PwMemberMoney extends Model<PwMemberMoney> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField("order_num")
    private String orderNum;

    /**
     * 会员ID
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 上一次余额
     */
    @TableField("before_balance")
    private BigDecimal beforeBalance;

    /**
     * 类型 1充值 2消费
     */
    private Integer type;

    /**
     * 额度
     */
    private BigDecimal quota;

    /**
     * 实际付款额度
     */
    @TableField("act_quota")
    private BigDecimal actQuota;

    /**
     * 模式 1微信 2支付宝 3现金 4其他
     */
    @TableField("pay_type")
    private Integer payType;

    /**
     * 备注
     */
    private String remark;

    @TableField("create_time")
    private Date createTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
