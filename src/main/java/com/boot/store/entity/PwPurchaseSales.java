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
 * @since 2020-09-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pw_purchase_sales")
public class PwPurchaseSales extends Model<PwPurchaseSales> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单号
     */
    @TableField("order_num")
    private String orderNum;

    /**
     * 商品ID
     */
    @TableField("commodity_id")
    private Long commodityId;

    /**
     * 数量
     */
    private Integer count;


    /**
     * 渠道ID
     */
    @TableField("channel_id")
    private Long channelId;

    /**
     * 会员ID
     */
    @TableField("member_id")
    private Long memberId;

    /**
     * 额度
     */
    private BigDecimal quota;

	/**
	 * 商品进价
	 */
	private BigDecimal purchaseQuota;
    /**
     * 1会员 2普通客户
     */
    @TableField("consumer_type")
    private Integer consumerType;

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
