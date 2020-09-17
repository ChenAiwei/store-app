package com.boot.store.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import com.boot.store.dto.auth.ValidationGroups;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 
 * </p>
 *
 * @author auto
 * @since 2020-09-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pw_pet")
public class PwPet extends Model<PwPet> {

    private static final long serialVersionUID = 1L;
	@NotNull(message = "id 不允许为空",groups = {ValidationGroups.Editer.class})
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;


    /**
     * 宠物名
     */
	@NotNull(message = "name 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	private String name;

    /**
     * 宠物品种
     */
	@NotNull(message = "petTypeId 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("pet_type_id")
    private Long petTypeId;

    /**
     * 宠物图片
     */
    private String picture;

    /**
     * 1自养 2三方店铺 3其他
     */
	@NotNull(message = "source 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	private Integer source;

    /**
     * 1在店 2外出 3已售出 4其他
     */
	@NotNull(message = "status 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	private Integer status;

    /**
     * 入店时间
     */
	@NotNull(message = "startTime 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("start_time")
    private Date startTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 售价
     */
	@NotNull(message = "sellPrice 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("sell_price")
    private BigDecimal sellPrice;

    /**
     * 进价
     */
	@NotNull(message = "purchasePrice 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("purchase_price")
    private BigDecimal purchasePrice;

    /**
     * 售出时间
     */
    @TableField("sell_date")
    private Date sellDate;

    @TableField("create_time")
    private Date createTime;

    /**
     * 0未删除 1已删除
     */
    private Integer deleted;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
