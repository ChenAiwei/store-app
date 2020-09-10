package com.boot.store.context;

import com.boot.store.entity.TUser;

/**
 * @Author：chenaiwei
 * @Description：用户上下文
 * @CreateDate：2020/9/10 11:02
 */
public class UserContextHolder {
	public static ThreadLocal<TUser> context = new ThreadLocal<TUser>();

	public static TUser currentUser() {
		return context.get();
	}

	public static void set(TUser user) {
		context.set(user);
	}

	public static void shutdown() {
		context.remove();
	}
}
