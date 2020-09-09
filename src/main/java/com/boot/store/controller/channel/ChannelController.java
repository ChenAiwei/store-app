package com.boot.store.controller.channel;

import com.boot.store.annotation.Log;
import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.dto.plugin.XmSelectModelDto;
import com.boot.store.entity.PwChannel;
import com.boot.store.enums.LogEnum;
import com.boot.store.exception.ServiceException;
import com.boot.store.service.channel.IPwChannelService;
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
 * @Description：ChannelController
 * @CreateDate：2020/9/8 10:24
 */
@RestController
@RequestMapping("/channel")
public class ChannelController {

	@Autowired
	private IPwChannelService channelService;

	@GetMapping("/list")
	public ResultVo<PageVo<PwChannel>> list(@RequestParam(name = "page",defaultValue = "1") Integer page, @RequestParam (name = "limit",defaultValue = "20") Integer limit, String name, String type){
		PageVo<PwChannel> pageVo = channelService.list(page,limit,name,type);
		return ResultVoUtil.success(pageVo);
	}

	@GetMapping("/listAll")
	public ResultVo<List<XmSelectModelDto>> listAll(){
		List<XmSelectModelDto> resultList = new ArrayList<>();
		List<PwChannel> list = channelService.list(null);
		list.forEach(l->{
			resultList.add(new XmSelectModelDto(l.getChannelName(),l.getId()));
		});
		return ResultVoUtil.success(resultList);
	}

	@GetMapping("/getChannel")
	public ResultVo<PwChannel> getChannel (@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不能为空！");
		}
		return ResultVoUtil.success(channelService.getById(Long.valueOf(id)));
	}

	@PostMapping("/add")
	@Log(option = "渠道添加",type = LogEnum.ADD)
	public ResultVo<?> add(@RequestBody @Validated(ValidationGroups.Register.class) PwChannel channel){
		channel.setCreateTime(new Date());
		channelService.save(channel);
		return ResultVoUtil.success();
	}

	@PostMapping("/edit")
	@Log(option = "渠道编辑",type = LogEnum.EDIT)
	public ResultVo<?> edit(@RequestBody @Validated(ValidationGroups.Editer.class) PwChannel channel){
		channelService.updateById(channel);
		return ResultVoUtil.success();
	}

	@GetMapping("/delete")
	@Log(option = "删除渠道",type = LogEnum.DEL)
	public ResultVo<?> delete(@RequestParam String id){
		if (StringUtils.isBlank(id)){
			throw new ServiceException("id不能为空！");
		}
		channelService.deleteById(id);
		return ResultVoUtil.success();
	}
}
