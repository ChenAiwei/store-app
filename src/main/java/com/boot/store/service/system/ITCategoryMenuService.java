package com.boot.store.service.system;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.entity.TCategoryMenu;
import com.boot.store.vo.menu.MenuVo;

import java.util.List;

/**
 * <p>
 * 资源表 服务类
 * </p>
 *
 * @author aiwei
 * @since 2020-04-02
 */
public interface ITCategoryMenuService extends IService<TCategoryMenu> {

	/**
	 * 树形菜单信息
	 * @return
	 * @param checkMenuIdList
	 */
	List<MenuVo> menuTree(List<String> checkMenuIdList);
}
