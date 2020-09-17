package com.boot.store.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

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
@TableName("pw_pet_beauty_care")
public class PwPetBeautyCare extends Model<PwPetBeautyCare> {

    private static final long serialVersionUID = 1L;


	@NotNull(message = "id 不允许为空",groups = {ValidationGroups.Editer.class})
	@TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 服务名
     */
	@NotNull(message = "project_name 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("project_name")
    private String projectName;

    /**
     * 服务的宠物类型
     */
	@NotNull(message = "petType 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	@TableField("pet_type")
    private Long petType;

    /**
     * 费用
     */
	@NotNull(message = "money 不允许为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	private BigDecimal money;

    /**
     * 备注
     */
    private String remark;

    private Integer deleted;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
