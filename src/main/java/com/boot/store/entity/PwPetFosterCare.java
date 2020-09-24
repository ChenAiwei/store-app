package com.boot.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.boot.store.dto.auth.ValidationGroups;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author auto
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pw_pet_foster_care")
public class PwPetFosterCare extends Model<PwPetFosterCare> {

    private static final long serialVersionUID = 1L;

	@NotNull(message = "id 不允许为空",groups = {ValidationGroups.Editer.class})
	@TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 宠物名
     */
	@NotNull(message = "petName 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("pet_name")
    private String petName;

    /**
     * 订单号
     */
    @TableField("order_num")
    private String orderNum;

    /**
     * 开始时间
     */
	@NotNull(message = "startTime 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("start_time")
    private Date startTime;

    /**
     * 结束时间
     */
	@NotNull(message = "endTime 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("end_time")
    private Date endTime;

    /**
     * 联系人
     */
	@NotNull(message = "contactPerson 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("contact_person")
    private String contactPerson;

    /**
     * 联系人号码
     */
    @TableField("phone_num")
    private String phoneNum;

    /**
     * 备注
     */
    private String remark;

    /**
     * 1未开始 2 寄养中 3已结束
     */
	private Integer status;

    /**
     * 费用/天
     */
	@NotNull(message = "price 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	private BigDecimal price;

    /**
     * 宠物图片
     */
    @TableField("pet_picture")
    private String petPicture;

    /**
     * 协议图片
     */
    private String picture;

    @TableField("create_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;

    /**
     * 0未删除 1已删除
     */
    private Integer deleted;

	@TableField(exist = false)
	private Long dayCount;

	@TableField(exist = false)
	private BigDecimal sumPrice;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
