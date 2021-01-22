package com.boot.store.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.store.dto.auth.CategoryMenuDto;
import com.boot.store.dto.auth.RoleDto;
import com.boot.store.dto.auth.UserAuthDto;
import com.boot.store.dto.auth.UserRoleDto;
import com.boot.store.entity.TCategoryMenu;
import com.boot.store.entity.TRole;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.UserAuthCustomMapper;
import com.boot.store.service.system.ITCategoryMenuService;
import com.boot.store.service.system.ITRoleService;
import com.boot.store.service.system.IUserAuthService;
import com.boot.store.utils.JsonUtils;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.menu.MenuEditVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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

	@Autowired
	private ITRoleService roleService;

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

	@Override
	public List<String> menuLevelList(String uid) {
		List<UserRoleDto> userRoleDtos = userAuthCustomMapper.userRole(uid);
		UserRoleDto userRoleDto = userRoleDtos.get(0);
		List<TCategoryMenu> tCategoryMenus = new ArrayList<>();
		if (null != userRoleDto){
			userRoleDto.getRoleList().forEach(role ->{
				String categoryMenuUids = role.getCategoryMenuUids();
				if (StringUtils.isNotBlank(categoryMenuUids)){
					tCategoryMenus.addAll(getMenuList(categoryMenuUids));
				}
			});
		}
		return tCategoryMenus.isEmpty()?new ArrayList<String>():tCategoryMenus.stream().filter(menu ->StringUtils.isNotBlank(menu.getMenuLevel())).map(m ->m.getMenuLevel()).distinct().collect(Collectors.toList());
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

	@Override
	public void add(MenuEditVo menuEditVo) {
		TCategoryMenu categoryMenu = new TCategoryMenu();
		categoryMenu.setUid(UUIDUtils.genUid());
		categoryMenu.setParentUid(menuEditVo.getPid());
		categoryMenu.setName(menuEditVo.getName());
		categoryMenu.setTag(menuEditVo.getTag());
		categoryMenu.setUrl(menuEditVo.getHref());
		categoryMenu.setIcon(menuEditVo.getIcon());
		categoryMenu.setSort(Integer.valueOf(menuEditVo.getSort()));
		categoryMenu.setStatus(menuEditVo.getStatus() == 1?true:false);
		categoryMenu.setMenuType(Integer.valueOf(menuEditVo.getMenuType()));
		categoryMenu.setCreateTime(new Date());
		categoryMenuService.save(categoryMenu);
	}

	@Override
	public MenuEditVo item(String id) {
		TCategoryMenu categoryMenu = categoryMenuService.getById(id);
		if (null == categoryMenu){
			throw new ServiceException("菜单权限不存在！");
		}
		MenuEditVo menuEditVo = MenuEditVo.builder().id(categoryMenu.getUid())
				.pid(categoryMenu.getParentUid())
				.name(categoryMenu.getName())
				.tag(categoryMenu.getTag())
				.href(categoryMenu.getUrl())
				.icon(categoryMenu.getIcon())
				.sort(categoryMenu.getSort().toString())
				.status(categoryMenu.getStatus() ? 1 : 0)
				.menuType(categoryMenu.getMenuType().toString()).build();
		return menuEditVo;
	}

	@Override
	public void edit(MenuEditVo menuEditVo) {
		TCategoryMenu categoryMenu = categoryMenuService.getById(menuEditVo.getId());
		if (null == categoryMenu){
			throw new ServiceException("菜单权限不存在！");
		}
		categoryMenu.setName(menuEditVo.getName());
		categoryMenu.setTag(menuEditVo.getTag());
		categoryMenu.setUrl(menuEditVo.getHref());
		categoryMenu.setIcon(menuEditVo.getIcon());
		categoryMenu.setSort(Integer.valueOf(menuEditVo.getSort()));
		categoryMenu.setStatus(menuEditVo.getStatus() == 1?true:false);
		categoryMenu.setMenuType(Integer.valueOf(menuEditVo.getMenuType()));
		categoryMenu.setUpdateTime(new Date());
		categoryMenuService.updateById(categoryMenu);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public Boolean del(String id) {
		List<TCategoryMenu> categoryMenuList = categoryMenuService.list(new QueryWrapper<TCategoryMenu>().eq("parent_uid", id));
		List<String> idList = categoryMenuList.stream().map(categoryMenu -> categoryMenu.getUid()).collect(Collectors.toList());
		idList.add(id);
		List<TRole> roleList = roleService.list(new QueryWrapper<TRole>());
		roleList.forEach(role ->{
			String categoryMenuUids = role.getCategoryMenuUids();
			if (StringUtils.isNotEmpty(categoryMenuUids)){
				ArrayList<String> list = (ArrayList<String>) JsonUtils.jsonArrayToArrayList(categoryMenuUids);
				list.forEach(l ->{
					if (idList.contains(l)){
						throw new ServiceException("删除失败，当前菜单下有角色绑定，角色为：<font color='red'>"+role.getRoleName()+"</font>，请先手动解绑再进行删除操作！");
					}
				});
			}
		});
		return categoryMenuService.removeByIds(idList);
	}
}
