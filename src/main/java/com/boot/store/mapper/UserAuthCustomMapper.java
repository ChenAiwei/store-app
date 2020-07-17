package com.boot.store.mapper;

import com.boot.store.dto.auth.UserRoleDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：UserAuthCustomMapper
 * @CreateDate：2020/4/3 9:07
 */
public interface UserAuthCustomMapper {
	/**
	 * uid获取用户的权限信息
	 * @param uid
	 * @return
	 */
	List<UserRoleDto> userRole(@Param("uid") String uid);
}
