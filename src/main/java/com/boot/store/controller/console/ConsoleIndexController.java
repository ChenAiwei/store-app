package com.boot.store.controller.console;

import com.boot.store.dto.console.ConsoleViewDto;
import com.boot.store.service.statistics.IStatisticsService;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

/**
 * @Author：chenaiwei
 * @Description：ConsoleIndexController
 * @CreateDate：2020/10/13 9:58
 */
@RestController
@RequestMapping("/console")
public class ConsoleIndexController {
	@Autowired
	private IStatisticsService statisticsService;

	@GetMapping("/views")
	public ResultVo<ConsoleViewDto> views() throws ExecutionException, InterruptedException {
		ConsoleViewDto consoleViewDto = statisticsService.viewAll();
		return ResultVoUtil.success(consoleViewDto);
	}
}
