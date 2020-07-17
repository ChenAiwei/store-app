package com.boot.store.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author aiwei
 * @since 2020-04-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_role")
public class TRole extends Model<TRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色id
     */
    @TableId(value = "uid")
    private String uid;

    /**
     * 角色名
     */
	@TableField("role_name")
    private String roleName;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 状态
     */
    private Boolean status;

    /**
     * 角色介绍
     */
    private String summary;

    /**
     * 角色管辖的菜单的UID
     */
    @TableField("category_menu_uids")
    private String categoryMenuUids;


    @Override
    protected Serializable pkVal() {
        return this.uid;
    }

}
