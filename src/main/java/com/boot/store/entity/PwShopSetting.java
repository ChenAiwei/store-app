package com.boot.store.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
 * @since 2020-09-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pw_shop_setting")
public class PwShopSetting extends Model<PwShopSetting> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotNull(message = "name 不允许为空！")
    private String name;

    private String address;

    @TableField("concat_name")
    private String concatName;

    @TableField("concat_phone")
    private String concatPhone;

	/**
	 * 店铺锁屏密码
	 */
	@TableField("lock_password")
	private String lockPassword;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
