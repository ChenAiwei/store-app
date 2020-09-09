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
 * @since 2020-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pw_channel")
public class PwChannel extends Model<PwChannel> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
	@NotNull(message = "渠道名不能为空",groups = {ValidationGroups.Editer.class})
	private Long id;

    /**
     * 供应商名称
     */
	@NotNull(message = "渠道名不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	@TableField("channel_name")
    private String channelName;

    /**
     * 供应商地址
     */
    private String address;

    /**
     * 联系人
     */
	@NotNull(message = "联系人不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private String name;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型 1厂家直供 2第三方店铺渠道 3其他
     */
	@NotNull(message = "类型不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private Integer type;

    @TableField("create_time")
    private Date createTime;


	private Integer deleted;

	@Override
    protected Serializable pkVal() {
        return this.id;
    }

}
