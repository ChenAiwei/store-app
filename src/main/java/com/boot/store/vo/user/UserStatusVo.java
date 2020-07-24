package com.boot.store.vo.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：UserStatusVo
 * @CreateDate：2020/7/24 14:43
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStatusVo implements Serializable {
	@NotNull(message = "type类型不能为空")
	private Integer type;
	@NotEmpty(message = "用户ID不能为空")
	private List<String> uidList;
}
