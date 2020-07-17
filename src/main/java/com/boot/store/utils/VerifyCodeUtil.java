package com.boot.store.utils;

import com.boot.store.consts.StoreConst;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Author：chenaiwei
 * @Description：VerifyCodeUtil
 * @CreateDate：2020/7/9 8:46
 */
public class VerifyCodeUtil {

	public static String genCode(String type,Integer num) {
		StringBuilder codeBuilder = new StringBuilder();
		String[] letterSplit = StringUtils.split(StoreConst.LETTER_STR, ",");
		String[] numberSplit = StringUtils.split(StoreConst.NUMBER_STR, ",");
		List<String> letterList = Arrays.asList(letterSplit);
		List<String> numberList = Arrays.asList(numberSplit);
		List<String> resultList = new ArrayList<>();
		if (type.equals(StoreConst.VERIFY_CODE)){
			resultList.addAll(letterList);
			resultList.addAll(numberList);
		}else{
			resultList.addAll(numberList);
		}
		Collections.shuffle(resultList);
		Random random = new Random();
		for (int i=0;i<num;i++){
			codeBuilder.append(resultList.get(random.nextInt(resultList.size()-1))+",");
		}
		String strCode = codeBuilder.toString();
		return strCode.substring(0,strCode.length()-1);
	}
}
