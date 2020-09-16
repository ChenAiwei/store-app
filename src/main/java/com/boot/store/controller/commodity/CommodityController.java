package com.boot.store.controller.commodity;

import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.dto.commodity.CommodityDto;
import com.boot.store.dto.commodity.CommoditySellDto;
import com.boot.store.dto.commodity.CommoditySellRecordDto;
import com.boot.store.enums.LogEnum;
import com.boot.store.exception.ServiceException;
import com.boot.store.service.commodity.IPwCommodityService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.PageVo;
import com.boot.store.vo.ResultVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：CommodityController
 * @CreateDate：2020/9/8 16:08
 */
@RestController
@RequestMapping("/commodity")
public class CommodityController {

	@Autowired
	private IPwCommodityService commodityService;

	@GetMapping("/list")
	public ResultVo<PageVo<CommodityDto>> list(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String name, String channelId,String commodityTypeId){
		PageVo<CommodityDto> listDto = commodityService.list(page,limit,name,channelId,commodityTypeId);
		return ResultVoUtil.success(listDto);
	}

	@Log(option = "商品添加",type = LogEnum.ADD)
	@PostMapping("/add")
	public ResultVo<?> add(@RequestBody @Validated(ValidationGroups.Register.class) CommodityDto commodityDto){
		checkParams(commodityDto);
		commodityService.add(commodityDto);
		return ResultVoUtil.success();
	}

	@Log(option = "商品编辑",type = LogEnum.EDIT)
	@PostMapping("/edit")
	public ResultVo<?> edit(@RequestBody @Validated(ValidationGroups.Editer.class) CommodityDto commodityDto){
		checkParams(commodityDto);
		commodityService.edit(commodityDto);
		return ResultVoUtil.success();
	}

	@GetMapping("/getCommodity")
	public ResultVo<CommodityDto> getCommodity(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不能为空！");
		}
		CommodityDto commodityDto = commodityService.getCommodity(id);
		return ResultVoUtil.success(commodityDto);
	}

	@Log(option = "删除商品",type = LogEnum.DEL)
	@PostMapping("/delete")
	public ResultVo<?> delete(@RequestBody List<Long> idList){
		checkIdList(idList);
		commodityService.deleteByIds(idList);
		return ResultVoUtil.success();
	}

	@Log(option = "上架商品",type = LogEnum.EDIT)
	@PostMapping("/up")
	public ResultVo<?> up(@RequestBody List<Long> idList){
		checkIdList(idList);
		commodityService.up(idList);
		return ResultVoUtil.success();
	}

	@Log(option = "下架商品",type = LogEnum.EDIT)
	@PostMapping("/down")
	public ResultVo<?> down(@RequestBody List<Long> idList){
		checkIdList(idList);
		commodityService.down(idList);
		return ResultVoUtil.success();
	}

	@Log(option = "商品售出",type = LogEnum.ADD)
	@PostMapping("/sell")
	public ResultVo<?> sell(@RequestBody @Validated  CommoditySellDto commoditySellDto){
		commodityService.sell(commoditySellDto);
		return ResultVoUtil.success();
	}

	@GetMapping("/sellRecord")
	public ResultVo<PageVo<CommoditySellRecordDto>> sellRecord(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String name,String orderNum, String commodityNum,String channelId,String commodityTypeId,String id){
		PageVo<CommoditySellRecordDto> listSellRecord = commodityService.sellRecord(page,limit,orderNum,name,commodityNum,commodityTypeId,channelId,id);
		return ResultVoUtil.success(listSellRecord);
	}
	private void checkParams(CommodityDto commodityDto) {
		if (commodityDto.getDiscount() == 1 && commodityDto.getDiscountRate() == null){
			throw new ServiceException("商品折扣率不能为空！");
		}
		if (commodityDto.getDiscount() == 1 && commodityDto.getDiscountRate().compareTo(new BigDecimal(1) )== 1){
			throw new ServiceException("商品折扣率不能大于1！");
		}
		if (commodityDto.getSellPrice().compareTo(commodityDto.getPurchasePrice()) == -1 ){
			throw new ServiceException("商品售价不能低于进货价！");
		}
	}

	private void checkIdList(List<Long> idList) {
		if (CollectionUtils.isEmpty(idList)){
			throw new ServiceException("id不能为空！");
		}
	}

}
