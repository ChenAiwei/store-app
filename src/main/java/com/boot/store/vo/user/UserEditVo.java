package com.boot.store.vo.user;

import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.vo.role.RoleNameInfoVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：userAddVo
 * @CreateDate：2020/7/17 14:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditVo implements Serializable {
	@NotBlank(message = "uid 不允许为空",groups = ValidationGroups.Editer.class)
	private String uid;
	@NotBlank(message = "userName 不允许为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private String userName;
	private String nickName;
	private String phone;
	private String email;
	@NotBlank(message = "password 不允许为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private String password;
	private String birthday;
	@NotNull(message = "status 不允许为空")
	private Boolean status;
	@NotNull(message = "sex 不允许为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private Integer sex;
	private List<String> roleIdLits;
	private List<RoleNameInfoVo> roleList;
}
