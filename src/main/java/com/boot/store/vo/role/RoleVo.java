package com.boot.store.vo.role;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：RoleVo
 * @CreateDate：2020/7/15 16:25
 */
@Data
@Builder
public class RoleVo implements Serializable {
	private String id;
	private String name;
	private String remarks;
	private Integer status;
	private String createTime;
}
