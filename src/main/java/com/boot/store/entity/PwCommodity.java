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
 * @since 2020-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pw_commodity")
public class PwCommodity extends Model<PwCommodity> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品编号
     */
    @TableField("commodity_num")
    private String commodityNum;

    /**
     * 商品名
     */
    private String name;

    /**
     * 商品图
     */
    private String picture;

    /**
     * 商品数量
     */
    private Integer count;

	/**
	 * 商品售出数量
	 */
	private Integer sellCount;

    /**
     * 进价
     */
    @TableField("purchase_price")
    private BigDecimal purchasePrice;

    /**
     * 售价
     */
    @TableField("sell_price")
    private BigDecimal sellPrice;

    /**
     * 供应商ID
     */
    @TableField("channel_id")
    private Long channelId;

    /**
     * 商品类型ID
     */
    @TableField("commodity_type_id")
    private Integer commodityTypeId;

    /**
     * 0下架 1上架
     */
    private Integer status;

    /**
     * 0不打折 1打折
     */
    private Integer discount;

    /**
     * 折扣率
     */
    @TableField("discount_rate")
    private BigDecimal discountRate;

    /**
     * 是否促销热门商品 0否 1是
     */
    private Integer promotion;

    /**
     * 备注
     */
    private String remark;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    private Integer deleted;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
