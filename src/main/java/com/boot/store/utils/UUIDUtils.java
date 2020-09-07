package com.boot.store.utils;

import cn.hutool.core.util.IdUtil;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * @Author：chenaiwei
 * @Description：UUIDUtils 这里不使用开源的uuid生成器
 * @CreateDate：2020/7/6 15:52
 */
public class UUIDUtils {
	private UUIDUtils(){}

	/**
	 * 生成单实列下的唯一uuid
	 * @return
	 */
	public static String genUid(){
		synchronized (UUIDUtils.class){
			String timeMillis = String.valueOf(System.currentTimeMillis());
			String pre = timeMillis.substring(0, 5);
			String end = timeMillis.substring(timeMillis.length() - 4, timeMillis.length()-1);
			return pre+  IdUtil.simpleUUID().replace("-","").substring(0,8)+end;
		}
	}
	
	public static String genToken(String uid) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String start =  IdUtil.simpleUUID().substring(0, 8);
		String end =  IdUtil.simpleUUID().substring(0, 8);
		return start+MD5Util.getEncryptedPwd(uid)+end;
	}

	public static String genOrder(){
		synchronized (UUIDUtils.class){
			String timeMillis = String.valueOf(System.currentTimeMillis());
			String end = timeMillis.substring(timeMillis.length() - 4, timeMillis.length()-1);
			return timeMillis+end;
		}
	}
}
