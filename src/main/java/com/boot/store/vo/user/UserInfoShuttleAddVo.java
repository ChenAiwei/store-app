package com.boot.store.vo.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：UserInfoShuttleAddVo
 * @CreateDate：2020/7/16 16:56
 */
@Data
@Builder
public class UserInfoShuttleAddVo implements Serializable {
	@NotNull(message = "角色ID不能为空")
	private String id;
	private List<UserInfoVo> idList;
}
