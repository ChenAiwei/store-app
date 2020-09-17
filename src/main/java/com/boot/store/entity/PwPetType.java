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
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author auto
 * @since 2020-09-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pw_pet_type")
public class PwPetType extends Model<PwPetType> {

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
     * 1 猫 2 狗 3其他
     */
	@NotNull(message = "petType 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("pet_type")
    private Integer petType;

    /**
     * 备注
     */
    private String remark;

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
