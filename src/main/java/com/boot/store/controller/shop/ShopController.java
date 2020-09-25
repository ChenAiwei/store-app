package com.boot.store.controller.shop;

import com.boot.store.annotation.Log;
import com.boot.store.entity.PwShopSetting;
import com.boot.store.enums.LogEnum;
import com.boot.store.service.shop.IPwShopSettingService;
import com.boot.store.utils.MD5Util;
import com.boot.store.utils.ResultVoUtil;
import com.boot.store.vo.ResultVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author：chenaiwei
 * @Description：ShopController
 * @CreateDate：2020/9/24 19:53
 */
@RestController
@RequestMapping("/shop")
public class ShopController {

	@Autowired
	private IPwShopSettingService shopSettingService;
	private static final String LOCK_PWD = "123456";

	@GetMapping("/getShop")
	public ResultVo<PwShopSetting> get(){
		List<PwShopSetting> list = shopSettingService.list(null);
		if (CollectionUtils.isEmpty(list)){
			return ResultVoUtil.success(new PwShopSetting());
		}else{
			PwShopSetting pwShopSetting = list.get(0);
			pwShopSetting.setLockPassword("");
			return ResultVoUtil.success(pwShopSetting);
		}
	}

	@Log(type = LogEnum.EDIT,option = "店铺编辑")
	@PostMapping("/edit")
	public ResultVo<?> edit(@RequestBody @Validated PwShopSetting shopSetting) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String lockPassword = shopSetting.getLockPassword();
		if (StringUtils.isNotBlank(lockPassword)){
			shopSetting.setLockPassword(MD5Util.getEncryptedPwd(lockPassword));
		}
		shopSettingService.saveOrUpdate(shopSetting);
		return ResultVoUtil.success();
	}

	@GetMapping("/lock")
	public ResultVo<?> lock(@RequestParam String pwd){
		List<PwShopSetting> list = shopSettingService.list(null);
		if (CollectionUtils.isEmpty(list)){
			if (LOCK_PWD.equals(pwd)){
				return ResultVoUtil.success();
			}else {
				return ResultVoUtil.error("密码错误");
			}
		}else {
			PwShopSetting pwShopSetting = list.get(0);
			if (StringUtils.isBlank(pwShopSetting.getLockPassword())){
				if (LOCK_PWD.equals(pwd)){
					return ResultVoUtil.success();
				}else {
					return ResultVoUtil.error("密码错误");
				}
			}else{
				if(MD5Util.validPassword(pwd,pwShopSetting.getLockPassword())){
					return ResultVoUtil.success();
				}else {
					return ResultVoUtil.error("密码错误");
				}
			}
		}
	}
}
