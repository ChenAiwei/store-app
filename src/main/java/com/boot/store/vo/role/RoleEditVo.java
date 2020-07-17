package com.boot.store.vo.role;

import com.boot.store.vo.menu.MenuVo;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：RoleEditVo
 * @CreateDate：2020/7/16 11:20
 */
@Data
@Builder
public class RoleEditVo implements Serializable {
	private String id;
	private String name;
	private String remarks;
	private Integer status;
	private List<MenuVo> menuList;
}
