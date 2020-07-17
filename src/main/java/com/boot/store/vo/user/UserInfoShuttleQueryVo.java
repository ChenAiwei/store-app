package com.boot.store.vo.user;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：UserInfoShuttleVo
 * @CreateDate：2020/7/16 16:50
 */
@Data
@Builder
public class UserInfoShuttleQueryVo implements Serializable {
	private List<UserInfoVo> shuttleList;
	private List<String> valueList;
}
