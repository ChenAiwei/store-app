package com.boot.store.vo.menu;

import com.boot.store.dto.auth.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：MenuEditVo
 * @CreateDate：2020/7/29 9:29
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuEditVo implements Serializable {
	private String pid;
	@NotBlank(message = "id 不允许为空",groups = ValidationGroups.Editer.class)
	private String id;
	@NotBlank(message = "name 不允许为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private String name;
	private String tag;
	@NotNull(message = "href 不允许为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private String href;
	private String sort;
	private String icon;
	@NotNull(message = "status 不允许为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private Integer status;
	@NotNull(message = "menuType 不允许为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private String menuType;
}
