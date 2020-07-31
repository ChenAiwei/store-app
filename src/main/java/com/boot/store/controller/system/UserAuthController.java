package com.boot.store.controller.system;

import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.CategoryMenuDto;
import com.boot.store.dto.auth.UserAuthDto;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.enums.LogEnum;
import com.boot.store.service.system.IUserAuthService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.ResultVo;
import com.boot.store.vo.menu.MenuEditVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：UserAuthController
 * @CreateDate：2020/4/3 8:42
 */
@RestController
@RequestMapping("/auth/info")
@Slf4j
public class UserAuthController {

	@Autowired
	private IUserAuthService userAuthService;

	/**
	 * 根据uid获取用户的所有权限信息
	 * @param userId
	 * @return
	 */
	@GetMapping("/list")
	public ResultVo<List<UserAuthDto>> list(@RequestParam String userId){
			return ResultVoUtil.success(userAuthService.info(userId));
	}

	@GetMapping("/listAll")
	public ResultVo<List<UserAuthDto>> listAll(){
		return ResultVoUtil.success(userAuthService.info(null));
	}

	@GetMapping("/tree")
	public ResultVo<List<CategoryMenuDto>> tree(){
		List<CategoryMenuDto> parentList  = new ArrayList<>();
		CategoryMenuDto categoryMenuDto = new CategoryMenuDto();
		categoryMenuDto.setName("根目录");
		categoryMenuDto.setIsShow(true);
		categoryMenuDto.setChildMenuList(userAuthService.tree());
		parentList.add(categoryMenuDto);
		return ResultVoUtil.success(parentList);
	}

	@GetMapping("/uid/{id}")
	public ResultVo<MenuEditVo> item(@PathVariable String id){
		return ResultVoUtil.success(userAuthService.item(id));
	}

	@Log(option = "添加菜单",type = LogEnum.ADD)
	@PostMapping("/add")
	public ResultVo<?> add(@Validated(ValidationGroups.Register.class) @RequestBody MenuEditVo menuEditVo){
		userAuthService.add(menuEditVo);
		return ResultVoUtil.success();
	}

	@Log(option = "编辑菜单",type = LogEnum.EDIT)
	@PostMapping("/edit")
	public ResultVo<?> edit(@Validated(ValidationGroups.Editer.class) @RequestBody MenuEditVo menuEditVo){
		userAuthService.edit(menuEditVo);
		return ResultVoUtil.success();
	}

	@Log(option = "删除菜单",type = LogEnum.DEL)
	@GetMapping("/del/{id}")
	public ResultVo<MenuEditVo> del(@PathVariable String id){
		return ResultVoUtil.success(userAuthService.del(id));
	}
}
