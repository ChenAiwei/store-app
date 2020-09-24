package com.boot.store.controller.pet;

import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.dto.pet.UploadPictureDto;
import com.boot.store.dto.plugin.PicturePreViewModelDto;
import com.boot.store.entity.PwPetFosterCare;
import com.boot.store.enums.LogEnum;
import com.boot.store.exception.ServiceException;
import com.boot.store.service.pet.IPwPetFosterCareService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author：chenaiwei
 * @Description：PwPetFosterCareController
 * @CreateDate：2020/9/21 16:33
 */
@RestController
@RequestMapping("/pet/fosterCare")
public class PwPetFosterCareController {

	@Autowired
	private IPwPetFosterCareService fosterCareService;

	@GetMapping("/list")
	public ResultVo<PageVo<PwPetFosterCare>> list(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String name, String concatName,String phone,String orderNum,String status){
		PageVo<PwPetFosterCare> list = fosterCareService.list(page,limit,name,concatName,phone,orderNum,status);
		return ResultVoUtil.success(list);
	}

	@GetMapping("/getById")
	public ResultVo<PwPetFosterCare> getById(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不允许为空！");
		}
		return ResultVoUtil.success(fosterCareService.getById(Long.valueOf(id)));
	}

	@Log(type = LogEnum.ADD,option = "宠物寄养添加")
	@PostMapping("/add")
	public ResultVo<?> add(@RequestBody @Validated(ValidationGroups.Register.class) PwPetFosterCare fosterCare){
		fosterCareService.add(fosterCare);
		return ResultVoUtil.success();
	}

	@Log(type = LogEnum.EDIT,option = "宠物寄养更新")
	@PostMapping("/edit")
	public ResultVo<?> edit(@RequestBody @Validated(ValidationGroups.Editer.class) PwPetFosterCare fosterCare){
		fosterCareService.edit(fosterCare);
		return ResultVoUtil.success();
	}

	@Log(type = LogEnum.DEL,option = "宠物寄养删除")
	@GetMapping("/delete")
	public ResultVo<PwPetFosterCare> delete(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不允许为空！");
		}
		fosterCareService.delete(Long.valueOf(id));
		return ResultVoUtil.success();
	}

	@GetMapping("/end")
	public ResultVo<PwPetFosterCare> end(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不允许为空！");
		}
		fosterCareService.end(Long.valueOf(id));
		return ResultVoUtil.success();
	}

	@PostMapping("/uploadPic")
	public ResultVo<?> uploadPic(@RequestBody @Validated UploadPictureDto uploadPictureDto){
		fosterCareService.uploadPic(uploadPictureDto);
		return ResultVoUtil.success();
	}

	@GetMapping("/preView")
	public ResultVo<PicturePreViewModelDto> preView (@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不允许为空！");
		}
		PicturePreViewModelDto preViewModelDto = fosterCareService.preView(Long.valueOf(id));
		return ResultVoUtil.success(preViewModelDto);
	}
}
