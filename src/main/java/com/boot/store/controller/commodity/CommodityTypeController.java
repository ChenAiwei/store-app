package com.boot.store.controller.commodity;

import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.dto.plugin.XmSelectModelDto;
import com.boot.store.entity.PwCommodityType;
import com.boot.store.enums.LogEnum;
import com.boot.store.exception.ServiceException;
import com.boot.store.service.commodity.IPwCommodityTypeService;
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
 * @Description：CommodityController
 * @CreateDate：2020/9/8 14:56
 */
@RestController
@RequestMapping("/commodity/type")
public class CommodityTypeController {
	@Autowired
	private IPwCommodityTypeService commodityTypeService;

	@GetMapping("/list")
	public ResultVo<PageVo<PwCommodityType>> list(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String name, String type){
		PageVo<PwCommodityType> resultVo = commodityTypeService.list(page,limit,name,type);
		return ResultVoUtil.success(resultVo);
	}

	@GetMapping("/listAll")
	public ResultVo<List<XmSelectModelDto>> listAll(){
		List<XmSelectModelDto> resultList = new ArrayList<>();
		List<PwCommodityType> list = commodityTypeService.list(null);
		list.forEach(l ->{
			resultList.add(new XmSelectModelDto(l.getName(),l.getId()));
		});
		return ResultVoUtil.success(resultList);
	}

	@GetMapping("/getCommodityType")
	public ResultVo<PwCommodityType> getCommodityType(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不允许为空！");
		}
		return ResultVoUtil.success(commodityTypeService.getById(Long.valueOf(id)));
	}

	@Log(option = "添加商品类型",type = LogEnum.ADD)
	@PostMapping("/add")
	public ResultVo<?> add(@RequestBody @Validated (ValidationGroups.Register.class) PwCommodityType commodityType){
		commodityType.setCreateTime(new Date());
		commodityTypeService.save(commodityType);
		return ResultVoUtil.success();
	}

	@Log(option = "编辑商品类型",type = LogEnum.EDIT)
	@PostMapping("/edit")
	public ResultVo<?> edit(@RequestBody @Validated (ValidationGroups.Editer.class) PwCommodityType commodityType){
		commodityTypeService.updateById(commodityType);
		return ResultVoUtil.success();
	}

	@Log(option = "删除商品类型",type = LogEnum.DEL)
	@GetMapping("/delete")
	public ResultVo<?> delete(@RequestParam String id) {
		if (StringUtils.isBlank(id)) {
			throw new ServiceException("id不允许为空！");
		}
		commodityTypeService.deleteById(Long.valueOf(id));
		return ResultVoUtil.success();
	}
}
