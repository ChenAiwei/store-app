package com.boot.store.component;

import cn.hutool.json.JSONUtil;
import com.boot.store.dto.plugin.MessagePushDto;
import com.boot.store.entity.TUser;
import com.boot.store.service.system.ITUserService;
import com.boot.store.service.system.impl.TUserServiceImpl;
import com.boot.store.utils.EhcacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author：chenaiwei
 * @Description：WebSocketServer
 * @CreateDate：2020/9/25 10:51
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{id}/{token}")
public class WebSocketServer {

	private EhcacheUtil cacheUtil;

	private ITUserService userService;
	/**
	 * 与某个客户端的连接会话，需要通过它来给客户端发送数据
	 */
	private Session session;

	/**
	 * 记录当前在线连接数(为保证线程安全，须对使用此变量的方法加lock或synchronized)
	 */
	private static int onlineCount = 0;

	/**
	 * 用来存储当前在线的客户端(此map线程安全)
	 */
	private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

	/**
	 * 连接建立成功后调用
	 */
	@OnOpen
	public void onOpen(@PathParam(value = "id") String id,@PathParam(value = "token") String token, Session session) throws IOException {
		this.session = session;
		cacheUtil = (EhcacheUtil) ApplicationContextProvider.getBean(EhcacheUtil.class);
		userService = (ITUserService) ApplicationContextProvider.getBean(TUserServiceImpl.class);
		if (StringUtils.isBlank(cacheUtil.get(id))){
			sendMessage(MessagePushDto.builder()
					.content("用户未登录！")
					.needShow(false)
					.build());
			session.close();
			log.info("用户未登录，关闭ws连接！");
			return;
		}
		if (!cacheUtil.get(id).equals(token)) {
			sendMessage(MessagePushDto.builder()
					.content("token验证失败！")
					.needShow(false)
					.build());
			session.close();
			log.info("token用户信息验证失败，关闭ws连接！");
			return;
		}
		TUser user = userService.getById(id);
		if (null == user || !user.getStatus()){
			sendMessage(MessagePushDto.builder()
					.content("用户信息验证失败！")
					.needShow(false)
					.build());
			session.close();
			log.info("用户信息验证失败，关闭ws连接！");
			return;
		}
		session.getUserProperties().put("userId",id);
		webSocketMap.put(id, this);
		addOnlineCount();
		log.info("客户端:" + id + ",加入，当前在线数为：" + getOnlineCount());
		try {
			sendMessage(MessagePushDto.builder()
					.content("WebSocket连接成功,当前身份："+id)
					.needShow(false)
					.build());
		} catch (IOException e) {
			log.error("WebSocket IO异常");
		}
	}

	/**
	 * 连接关闭时调用
	 */
	@OnClose
	public void onClose(Session session) {
		// 从map中删除
		webSocketMap.remove(session.getUserProperties().get("userId"));
		// 在线数减1
		subOnlineCount();
		log.info("客户端：{},连接关闭，当前在线数为：{}",session.getUserProperties().get("userId"),getOnlineCount());
	}

	/**
	 * 收到客户端消息后调用
	 * @param message 客户端发送过来的消息<br/>
	 * @param session 用户信息
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		log.info("来自客户端{}的消息:{}",session.getUserProperties().get("userId"),message );
	}

	/**
	 * 发生错误时回调
	 *
	 * @param session 用户信息
	 * @param error 错误
	 */
	@OnError
	public void onError(Session session, Throwable error) {
		log.error("WebSocket发生错误,客户端：{}",session.getUserProperties().get("userId"));
		error.printStackTrace();
	}

	/**
	 * 推送信息给指定ID客户端，如客户端不在线，则返回不在线信息给自己
	 *
	 * @param message 客户端发来的消息
	 * @param sendClientId 客户端ID
	 */
	public void sendToUser(MessagePushDto message, String sendClientId) throws IOException {
		if (webSocketMap.get(sendClientId) != null) {
			webSocketMap.get(sendClientId).sendMessage(message);
			log.info("发送消息给客户端{}，消息内容：",sendClientId,message);
		} else {
			log.info("当前客户端：{}，不在线",sendClientId);
		}
	}

	/**
	 * 群送发送信息给所有人
	 *
	 * @param message 要发送的消息
	 */
	public void sendToAll(MessagePushDto message) throws IOException {
		for (String key : webSocketMap.keySet()) {
			webSocketMap.get(key).sendMessage(message);
		}
		log.info("WebSocket广播消息,消息内容："+message);
	}

	/**
	 * 发送消息
	 * @param pushDto 要发送的消息
	 */
	private void sendMessage(MessagePushDto pushDto) throws IOException {
		this.session.getBasicRemote().sendText(JSONUtil.toJsonStr(pushDto));
	}

	/**
	 * 获取在线人数
	 * @return 在线人数
	 */
	private static synchronized int getOnlineCount() {
		return onlineCount;
	}

	/**
	 * 有人上线时在线人数加一
	 */
	private static synchronized void addOnlineCount() {
		WebSocketServer.onlineCount++;
	}

	/**
	 * 有人下线时在线人数减一
	 */
	private static synchronized void subOnlineCount() {
		if (WebSocketServer.onlineCount != 0){
			WebSocketServer.onlineCount--;
		}
	}
}
