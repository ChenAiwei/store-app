package com.boot.store.controller.pet;


import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.entity.PwPetBeautyCare;
import com.boot.store.enums.LogEnum;
import com.boot.store.exception.ServiceException;
import com.boot.store.service.pet.IPwPetBeautyCareService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/petBeautyCare")
public class PwPetBeautyCareController {

	@Autowired
	private IPwPetBeautyCareService petBeautyCareService;

	@GetMapping("/list")
	public ResultVo<PageVo<PwPetBeautyCare>> list(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String name, String typeId){
		PageVo<PwPetBeautyCare> list = petBeautyCareService.list(page,limit,typeId,name);
		return ResultVoUtil.success(list);
	}

	@GetMapping("/getById")
	public ResultVo<PwPetBeautyCare> getById(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不能为空！");
		}
		PwPetBeautyCare petBeautyCare = petBeautyCareService.getById(Long.valueOf(id));
		return ResultVoUtil.success(petBeautyCare);
	}

	@Log(type = LogEnum.ADD,option = "宠物服务添加")
	@PostMapping("/add")
	public ResultVo<?> add(@RequestBody @Validated  (ValidationGroups.Register.class) PwPetBeautyCare pwPetBeautyCare){
		petBeautyCareService.add(pwPetBeautyCare);
		return ResultVoUtil.success();
	}


	@Log(type = LogEnum.EDIT,option = "宠物服务编辑")
	@PostMapping("/edit")
	public ResultVo<?> edit(@RequestBody @Validated  (ValidationGroups.Register.class) PwPetBeautyCare pwPetBeautyCare){
		petBeautyCareService.edit(pwPetBeautyCare);
		return ResultVoUtil.success();
	}

	@Log(type = LogEnum.DEL,option = "宠物服务删除")
	@GetMapping("/delete")
	public ResultVo<PwPetBeautyCare> delete(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不能为空！");
		}
		petBeautyCareService.delete(Long.valueOf(id));
		return ResultVoUtil.success();
	}
}

