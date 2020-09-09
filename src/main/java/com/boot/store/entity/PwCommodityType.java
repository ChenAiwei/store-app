package com.boot.store.entity;

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
 * @since 2020-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pw_commodity_type")
public class PwCommodityType extends Model<PwCommodityType> {

    private static final long serialVersionUID = 1L;

	@NotNull(message = "id不能为空",groups = {ValidationGroups.Editer.class})
	@TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 类型名
     */
	@NotNull(message = "name不能为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	private String name;

    /**
     * 类型 1猫 2狗 3其他
     */
	@NotNull(message = "type不能为空",groups = {ValidationGroups.Editer.class,ValidationGroups.Register.class})
	private String type;

    @TableField("create_time")
    private Date createTime;

    private Integer deleted;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
