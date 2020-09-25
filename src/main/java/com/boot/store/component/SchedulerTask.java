package com.boot.store.component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.store.dto.plugin.MessagePushDto;
import com.boot.store.entity.PwPetFosterCare;
import com.boot.store.service.pet.IPwPetFosterCareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：SchedulerTask
 * @CreateDate：2020/9/25 15:36
 */
@Slf4j
@Component
public class SchedulerTask {

	@Autowired
	private WebSocketServer webSocketServer;

	@Autowired
	private IPwPetFosterCareService fosterCareService;

	/**
	 * 5分钟检查一次寄养宠物的到期时间
	 * @throws IOException
	 */
	@Scheduled(cron = "0 */5 * * * ?")
	public void checkPetBeautyCare(){
		List<PwPetFosterCare> list = fosterCareService.list(new QueryWrapper<PwPetFosterCare>().eq("status", 2).eq("deleted", 0));
		list.forEach(l ->{
			Date endTime = l.getEndTime();
			if ((new Date().after(endTime))){
				MessagePushDto pushDto = MessagePushDto.builder()
						.title("宠物寄养到期")
						.content(l.getPetName()+"寄养时间已到期，请在系统及时结束寄养状态")
						.needReply(false)
						.sendDateTime(DateUtil.formatDateTime(new Date()))
						.sender("system")
						.needShow(true).build();
				try {
					webSocketServer.sendToAll(pushDto);
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
