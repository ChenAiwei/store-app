package com.boot.store.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.store.dto.system.RoleNameDto;
import com.boot.store.entity.TRole;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author aiwei
 * @since 2020-04-02
 */
public interface TRoleMapper extends BaseMapper<TRole> {

	@MapKey("userId")
	Map<String, RoleNameDto> getNameByUserIdList(@Param("uidLists") List<String> uidLists);
}
