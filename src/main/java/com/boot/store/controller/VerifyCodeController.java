package com.boot.store.controller;

import com.boot.store.dto.VerifyCodeDto;
import com.boot.store.utils.EhcacheUtil;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.utils.VerifyCodeUtil;
import com.boot.store.vo.ResultVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Semaphore;

/**
 * @Author：chenaiwei
 * @Description：VerifyCodeController
 * @CreateDate：2020/7/8 16:28
 */
@RestController
@RequestMapping("/verify")
public class VerifyCodeController {
	private Semaphore semaphore=new Semaphore(1);
	@Autowired
	EhcacheUtil cacheUtil;

	@GetMapping("/getCode/{type}")
	public ResultVo<VerifyCodeDto> getCode(@PathVariable String type){
		VerifyCodeDto verifyCode = null;
		try {
			semaphore.acquire(1);
			String codeKey = UUIDUtils.genUid();
			String code = VerifyCodeUtil.genCode(type,4);
			cacheUtil.set(codeKey,code.replace(",",""),60);
			verifyCode = new VerifyCodeDto(codeKey,code);
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			semaphore.release(1);
		}
		return ResultVoUtil.success(verifyCode);
	}
}
