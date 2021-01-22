package com.boot.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.store.entity.TUser;
import com.boot.store.vo.user.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author aiwei
 * @since 2020-04-02
 */
public interface TUserMapper extends BaseMapper<TUser> {

	/**
	 * 用户列表分页查询
	 * @param page
	 * @param limit
	 * @param startTime
	 * @param endTime
	 * @param userName
	 * @param roleId
	 * @return
	 */
	List<UserVo> listUser(@Param("page") Integer page, @Param("limit") Integer limit, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userName")String userName, @Param("roleId") String roleId);

	/**
	 * 用户列表查询总数
	 * @param startTime
	 * @param endTime
	 * @param userName
	 * @param roleId
	 * @return
	 */
	Long listUserCount(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userName")String userName, @Param("roleId") String roleId);
}
