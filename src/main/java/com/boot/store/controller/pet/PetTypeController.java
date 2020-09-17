package com.boot.store.controller.pet;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.dto.plugin.XmSelectModelDto;
import com.boot.store.entity.PwPetType;
import com.boot.store.enums.LogEnum;
import com.boot.store.exception.ServiceException;
import com.boot.store.service.pet.IPwPetTypeService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：PetController
 * @CreateDate：2020/9/16 18:40
 */
@RestController
@RequestMapping("/petType")
public class PetTypeController {

	@Autowired
	private IPwPetTypeService petTypeService;

	@GetMapping("/list")
	public ResultVo<PageVo<PwPetType>> list(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String name, String type){
		PageVo<PwPetType> list = petTypeService.list(page,limit,type,name);
		return ResultVoUtil.success(list);
	}

	@GetMapping("/listAll")
	public ResultVo<List<XmSelectModelDto>> listAll(){
		List<XmSelectModelDto>  list = new ArrayList<>();
		List<PwPetType> petTypeList = petTypeService.list(new QueryWrapper<PwPetType>().eq("deleted", 0));
		petTypeList.forEach(petType -> {
			list.add(new XmSelectModelDto(petType.getPetName(),petType.getId()));
		});
		return ResultVoUtil.success(list);
	}

	@Log(type = LogEnum.ADD,option = "宠物类型添加")
	@PostMapping("/add")
	public ResultVo<?> add(@RequestBody @Validated (ValidationGroups.Register.class) PwPetType petType){
		petType.setCreateTime(new Date());
		petTypeService.save(petType);
		return ResultVoUtil.success();
	}

	@Log(type = LogEnum.EDIT,option = "宠物类型编辑")
	@PostMapping("/edit")
	public ResultVo<?> edit(@RequestBody @Validated (ValidationGroups.Editer.class) PwPetType petType){
		petTypeService.updateById(petType);
		return ResultVoUtil.success();
	}

	@Log(type = LogEnum.DEL,option = "宠物类型删除")
	@GetMapping("/delete")
	public ResultVo<?> delete(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不允许为空！");
		}
		petTypeService.deleteById(id);
		return ResultVoUtil.success();
	}

	@GetMapping("/getPetType")
	public ResultVo<PwPetType> getPetType(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不允许为空！");
		}
		return ResultVoUtil.success(petTypeService.getById(Long.valueOf(id)));
	}
}
