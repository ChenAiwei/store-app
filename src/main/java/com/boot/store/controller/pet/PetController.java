package com.boot.store.controller.pet;

import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.entity.PwPet;
import com.boot.store.enums.LogEnum;
import com.boot.store.exception.ServiceException;
import com.boot.store.service.pet.IPwPetService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：chenaiwei
 * @Description：PetController
 * @CreateDate：2020/9/17 14:24
 */
@RestController
@RequestMapping("/pet")
public class PetController {

	@Autowired
	private IPwPetService petService;

	@GetMapping("/list")
	public ResultVo<PageVo<PwPet>> list(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String name, String typeId){
		PageVo<PwPet> list = petService.list(page,limit,typeId,name);
		return ResultVoUtil.success(list);
	}

	@GetMapping("/getPet")
	public ResultVo<PwPet> getPet(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不能为空！");
		}
		PwPet pet = petService.getById(Long.valueOf(id));
		return ResultVoUtil.success(pet);
	}

	@Log(type = LogEnum.ADD,option = "宠物添加")
	@PostMapping("/addPet")
	public ResultVo<?> addPet(@RequestBody @Validated (ValidationGroups.Register.class) PwPet pet){
		petService.addPet(pet);
		return ResultVoUtil.success();
	}

	@Log(type = LogEnum.EDIT,option = "宠物编辑")
	@PostMapping("/editPet")
	public ResultVo<?> editPet(@RequestBody @Validated (ValidationGroups.Editer.class) PwPet pet){
		petService.editPet(pet);
		return ResultVoUtil.success();
	}

	@Log(type = LogEnum.DEL,option = "宠物删除")
	@GetMapping("/deletePet")
	public ResultVo<?> deletePet(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不能为空！");
		}
		petService.deletePet(id);
		return ResultVoUtil.success();
	}
}
