package com.boot.store.service.pet;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.store.dto.pet.UploadPictureDto;
import com.boot.store.dto.plugin.PicturePreViewModelDto;
import com.boot.store.entity.PwPetFosterCare;
import com.boot.store.vo.PageVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author auto
 * @since 2020-09-21
 */
public interface IPwPetFosterCareService extends IService<PwPetFosterCare> {

	/**
	 * 寄养列表
	 * @param page
	 * @param limit
	 * @param name
	 * @param concatName
	 * @param phone
	 * @param orderNum
	 * @return
	 */
	PageVo<PwPetFosterCare> list(Integer page, Integer limit, String name, String concatName, String phone, String orderNum,String status);

	/**
	 * 寄养添加
	 * @param fosterCare
	 */
	void add(PwPetFosterCare fosterCare);

	/**
	 * 寄养编辑
	 * @param fosterCare
	 */
	void edit(PwPetFosterCare fosterCare);

	/**
	 * 寄养删除
	 * @param id
	 */
	void delete(Long id);

	/**
	 * 结束寄养
	 * @param id
	 */
	void end(Long id);

	/**
	 * 图片上传 协议图
	 * @param uploadPictureDto
	 */
	void uploadPic(UploadPictureDto uploadPictureDto);

	/**
	 * 图片预览
	 * @param id
	 * @return
	 */
	PicturePreViewModelDto preView(Long id);
}
