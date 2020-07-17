package com.boot.store.service.system.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.TCategoryMenu;
import com.boot.store.mapper.TCategoryMenuMapper;
import com.boot.store.service.system.ITCategoryMenuService;
import com.boot.store.vo.menu.MenuVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author aiwei
 * @since 2020-04-02
 */
@Service
public class TCategoryMenuServiceImpl extends ServiceImpl<TCategoryMenuMapper, TCategoryMenu> implements ITCategoryMenuService {

	@Override
	public List<MenuVo> menuTree(List<String> menuIdList) {
		List<TCategoryMenu> tCategoryMenus = this.baseMapper.selectList(new QueryWrapper<TCategoryMenu>().eq("status", 1));
		List<MenuVo> menuVoList = new ArrayList<>();
		MenuVo rootMenu = MenuVo.builder().title("根目录").spread(true).build();
		rootMenu.setChildren(this.combineAndSortMenu(tCategoryMenus,menuIdList));
		menuVoList.add(rootMenu);
		return menuVoList;
	}


	/**
	 * 菜单 去重 排序 树形结构化
	 * @param tCategoryMenus
	 * @param menuIdList
	 * @return
	 */
	private List<MenuVo> combineAndSortMenu(List<TCategoryMenu> tCategoryMenus, List<String> menuIdList) {
		List<MenuVo> categoryMenuDtoList = new ArrayList<>();
		tCategoryMenus = tCategoryMenus.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TCategoryMenu :: getUid))), ArrayList::new));
		tCategoryMenus.forEach(categoryMenu ->{
			MenuVo vo = MenuVo.builder().id(categoryMenu.getUid())
					.pid(categoryMenu.getParentUid())
					.spread(false)
					.sort(categoryMenu.getSort())
					.title(categoryMenu.getName()).build();
			if (CollectionUtils.isNotEmpty(menuIdList)){
				vo.setChecked(menuIdList.contains(vo.getId()));
			}
			categoryMenuDtoList.add(vo);
		});
		return buildByRecursive(categoryMenuDtoList);
	}

	/**
	 * 使用递归方法建树
	 *
	 * @param treeNodes
	 * @return
	 */
	private List<MenuVo> buildByRecursive(List<MenuVo> treeNodes) {
		List<MenuVo> trees = new ArrayList<MenuVo>();
		for (MenuVo treeNode : treeNodes) {
			if (StringUtils.isBlank(treeNode.getPid())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		List<MenuVo> collector = treeSort(trees);
		return collector;
	}

	/**
	 * 升序递归排序
	 * @param trees
	 * @return
	 */
	private List<MenuVo> treeSort(List<MenuVo> trees) {
		List<MenuVo> collect = trees.stream().sorted((a, b) -> a.getSort() - b.getSort()).collect(Collectors.toList());
		collect.forEach(tree ->{
			if (CollectionUtils.isNotEmpty(tree.getChildren())){
				tree.setChildren(treeSort(tree.getChildren()));
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
	private MenuVo findChildren(MenuVo treeNode, List<MenuVo> treeNodes) {
		for (MenuVo it : treeNodes) {
			if (treeNode.getId().equals(it.getPid())) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<MenuVo>());
				}
				treeNode.getChildren().add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}
}
