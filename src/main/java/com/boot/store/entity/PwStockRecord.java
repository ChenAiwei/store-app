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
@TableName("pw_stock_record")
public class PwStockRecord extends Model<PwStockRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 产品ID
     */
    @TableField("commodity_id")
    private Long commodityId;

    /**
     * 变更类型 1增加 2减少
     */
    private Integer type;

    /**
     * 变更前库存数量
     */
    @TableField("before_count")
    private Integer beforeCount;

    /**
     * 当前库存数量
     */
    @TableField("after_count")
    private Integer afterCount;

    /**
     * 变更前进价单价
     */
    @TableField("before_price")
    private BigDecimal beforePrice;

    /**
     * 当前进价单价
     */
    @TableField("after_price")
    private BigDecimal afterPrice;

    /**
     * 变更前进货花费总额度
     */
    @TableField("before_money")
    private BigDecimal beforeMoney;

    /**
     * 当前进货花费总额度
     */
    @TableField("after_money")
    private BigDecimal afterMoney;

    /**
     * 操作人ID
     */
    @TableField("opt_id")
    private String optId;

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
