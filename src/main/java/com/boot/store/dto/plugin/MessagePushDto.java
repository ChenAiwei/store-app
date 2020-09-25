package com.boot.store.dto.plugin;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author：chenaiwei
 * @Description：MessagePushDto
 * @CreateDate：2020/9/25 15:25
 */
@Data
@Builder
public class MessagePushDto implements Serializable {
	/**
	 * 消息标题
	 */
	private String title;
	/**
	 * 消息内容
	 */
	private String content;
	/**
	 * 发送时间
	 */
	private String sendDateTime;
	/**
	 * 发送者
	 */
	private String sender;
	/**
	 * 是否需要答复
	 */
	private Boolean needReply;
	/**
	 * 是否需要展示
	 */
	private Boolean needShow;
}
