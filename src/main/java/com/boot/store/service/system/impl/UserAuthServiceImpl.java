package com.boot.store.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.store.dto.auth.CategoryMenuDto;
import com.boot.store.dto.auth.RoleDto;
import com.boot.store.dto.auth.UserAuthDto;
import com.boot.store.dto.auth.UserRoleDto;
import com.boot.store.entity.TCategoryMenu;
import com.boot.store.entity.TRole;
import com.boot.store.mapper.UserAuthCustomMapper;
import com.boot.store.service.system.ITCategoryMenuService;
import com.boot.store.service.system.IUserAuthService;
import com.boot.store.utils.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @Author：chenaiwei
 * @Description：UserAuthServiceImpl
 * @CreateDate：2020/4/3 9:05
 */
@Service
public class UserAuthServiceImpl implements IUserAuthService {

	@Autowired
	private UserAuthCustomMapper userAuthCustomMapper;

	@Autowired
	private ITCategoryMenuService categoryMenuService;

	@Override
	public List<UserAuthDto> info(String uid) {
		List<UserAuthDto> userAuthDtoList = new ArrayList<>();
		List<UserRoleDto> userRoleDtos = userAuthCustomMapper.userRole(uid);
		if (!userRoleDtos.isEmpty()){
			userRoleDtos.forEach(userRoleDto  ->{
				UserAuthDto userAuthDto = new UserAuthDto();
				BeanUtils.copyProperties(userRoleDto,userAuthDto);//合并用户信息
				List<TRole> roleList = userRoleDto.getRoleList();
				if (!roleList.isEmpty()){
					userAuthDto.setRoleList(combineRoleList(roleList));//合并角色信息
					userAuthDto.setCategoryMenuDtoList(combineMenuList(roleList));//合并菜单信息
				}
				userAuthDtoList.add(userAuthDto);
			});
		}
		return userAuthDtoList;
	}

	/**
	 * 角色赋值
	 * @param roleList
	 * @return
	 */
	private List<RoleDto> combineRoleList(List<TRole> roleList) {
		List<RoleDto> roleDtoList = new ArrayList<>();
		roleList.forEach(role->{
			RoleDto dto = new RoleDto();
			BeanUtils.copyProperties(role,dto);
			roleDtoList.add(dto);
		});
		return  roleDtoList;
	}
	/***
	 * 用户的菜单信息
	 * @param roleList
	 * @return
	 */
	private List<CategoryMenuDto> combineMenuList(List<TRole> roleList) {
		List<TCategoryMenu> tCategoryMenus = new ArrayList<>();
		roleList.forEach(role->{
			String categoryMenuUids = role.getCategoryMenuUids();
			if (StringUtils.isNotBlank(categoryMenuUids)){
				tCategoryMenus.addAll(getMenuList(categoryMenuUids));//查出用户的每个角色的菜单信息合并到一起
			}
		});
		//对合并在一起的菜单信息进行去重，组装成树结构化，并且排序（用户可能有多个角色）
		List<CategoryMenuDto> categoryMenuDtoList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(tCategoryMenus)){
			categoryMenuDtoList = combineAndSortMenu(tCategoryMenus);
		}
		return categoryMenuDtoList;
	}

	/**
	 * 每个角色的菜单信息
	 * @param categoryMenuUids
	 * @return
	 */
	private List<TCategoryMenu> getMenuList(String categoryMenuUids) {
		ArrayList<String> categoryMenuUidsList = (ArrayList<String>) JsonUtils.jsonArrayToArrayList(categoryMenuUids);
		List<TCategoryMenu> categoryMenus = categoryMenuService.list(new QueryWrapper<TCategoryMenu>().in("uid", categoryMenuUidsList).eq("status",1));
		return categoryMenus;
	}


	/**
	 * 菜单 去重 排序 树形结构化
	 * @param tCategoryMenus
	 * @return
	 */
	private List<CategoryMenuDto> combineAndSortMenu(List<TCategoryMenu> tCategoryMenus) {
		List<CategoryMenuDto> categoryMenuDtoList = new ArrayList<>();
		tCategoryMenus = tCategoryMenus.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TCategoryMenu :: getUid))), ArrayList::new));
		tCategoryMenus.forEach(categoryMenu ->{
			CategoryMenuDto dto = new CategoryMenuDto();
			BeanUtils.copyProperties(categoryMenu,dto);
			categoryMenuDtoList.add(dto);
		});
		return buildByRecursive(categoryMenuDtoList);
	}

	/**
	 * 使用递归方法建树
	 *
	 * @param treeNodes
	 * @return
	 */
	private List<CategoryMenuDto> buildByRecursive(List<CategoryMenuDto> treeNodes) {
		List<CategoryMenuDto> trees = new ArrayList<CategoryMenuDto>();
		for (CategoryMenuDto treeNode : treeNodes) {
			if (StringUtils.isBlank(treeNode.getParentUid())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		List<CategoryMenuDto> collector = treeSort(trees);
		return collector;
	}

	/**
	 * 升序递归排序
	 * @param trees
	 * @return
	 */
	private List<CategoryMenuDto> treeSort(List<CategoryMenuDto> trees) {
		List<CategoryMenuDto> collect = trees.stream().sorted((a, b) -> a.getSort() - b.getSort()).collect(Collectors.toList());
		collect.forEach(tree ->{
			if (CollectionUtils.isNotEmpty(tree.getChildMenuList())){
				tree.setChildMenuList(treeSort(tree.getChildMenuList()));
			}
		});
		return collect;
	}

	/**
	 * 递归查找子节点
	 *
	 * @param treeNodes
	 * @return
	 */
	private CategoryMenuDto findChildren(CategoryMenuDto treeNode, List<CategoryMenuDto> treeNodes) {
		for (CategoryMenuDto it : treeNodes) {
			if (treeNode.getUid().equals(it.getParentUid())) {
				if (treeNode.getChildMenuList() == null) {
					treeNode.setChildMenuList(new ArrayList<CategoryMenuDto>());
				}
				treeNode.getChildMenuList().add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}


	/**
	 * 系统的所有权限Tree结构
	 * @return
	 */
	@Override
	public List<CategoryMenuDto> tree() {
		List<TCategoryMenu> categoryMenus = categoryMenuService.list(new QueryWrapper<TCategoryMenu>());
		return this.combineAndSortMenu(categoryMenus);
	}
}
