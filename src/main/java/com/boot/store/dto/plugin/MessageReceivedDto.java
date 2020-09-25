package com.boot.store.dto.plugin;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：MessageRereceiveDto
 * @CreateDate：2020/9/25 15:28
 */
@Data
@Builder
public class MessageReceivedDto implements Serializable {
	/**
	 * 消息标题
	 */
	private String title;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 回复时间
	 */
	private String receiveDateTime;
	/**
	 * 回复者
	 */
	private String receiver;


}
