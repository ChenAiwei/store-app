package com.boot.store.service.pet.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.dto.pet.UploadPictureDto;
import com.boot.store.dto.plugin.PictureDataDto;
import com.boot.store.dto.plugin.PicturePreViewModelDto;
import com.boot.store.entity.PwPetFosterCare;
import com.boot.store.entity.PwPetSales;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.PwPetFosterCareMapper;
import com.boot.store.service.pet.IPwPetFosterCareService;
import com.boot.store.service.pet.IPwPetSalesService;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-09-21
 */
@Service
public class PwPetFosterCareServiceImpl extends ServiceImpl<PwPetFosterCareMapper, PwPetFosterCare> implements IPwPetFosterCareService {

	@Autowired
	private IPwPetSalesService salesService;

	@Override
	public PageVo<PwPetFosterCare> list(Integer page, Integer limit, String name, String concatName, String phone, String orderNum,String status) {
		QueryWrapper<PwPetFosterCare> queryWrapper = new QueryWrapper<PwPetFosterCare>().eq("deleted",0).orderByDesc("create_time");
		if (StringUtils.isNotBlank(name)){
			queryWrapper.like("pet_name",name);
		}
		if (StringUtils.isNotBlank(concatName)){
			queryWrapper.eq("contact_person",concatName);
		}
		if (StringUtils.isNotBlank(phone)){
			queryWrapper.eq("phone_num",phone);
		}
		if (StringUtils.isNotBlank(orderNum)){
			queryWrapper.eq("order_num",orderNum);
		}
		if (StringUtils.isNotBlank(status)){
			queryWrapper.eq("status",status);
		}
		IPage<PwPetFosterCare> resultPage = this.page(new Page<>(page,limit),queryWrapper);
		resultPage.getRecords().forEach(fosterCare ->{
			Date startTime = fosterCare.getStartTime();
			Date endTime = fosterCare.getEndTime();
			long between = DateUtil.between(startTime, endTime, DateUnit.DAY);
			if (between == 0){
				between = 1;
			}
			fosterCare.setDayCount(between);
			fosterCare.setSumPrice(fosterCare.getPrice().multiply(new BigDecimal(between)));
			fosterCare.setPicture(StringUtils.isBlank(fosterCare.getPicture())?"":"1");
		});
		return new PageVo<>(resultPage.getTotal(),resultPage.getRecords());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void add(PwPetFosterCare fosterCare) {
		Date startTime = fosterCare.getStartTime();
		Date endTime = fosterCare.getEndTime();
		if (DateUtil.compare(endTime,startTime) < 0){
			throw new ServiceException("结束时间不能小于开始时间！");
		}
		fosterCare.setOrderNum(UUIDUtils.genOrder());
		fosterCare.setStatus(2);
		fosterCare.setCreateTime(new Date());
		this.baseMapper.insert(fosterCare);
		long between = DateUtil.between(startTime, endTime, DateUnit.DAY);
		if (between == 0){
			between = 1;
		}
		PwPetSales petSales = new PwPetSales();
		this.saveSales(petSales,fosterCare,between);
	}

	@Override
	public void edit(PwPetFosterCare fosterCare) {
		Date startTime = fosterCare.getStartTime();
		Date endTime = fosterCare.getEndTime();
		if (DateUtil.compare(endTime,startTime) < 0){
			throw new ServiceException("结束时间不能小于开始时间！");
		}
		long between = DateUtil.between(startTime, endTime, DateUnit.DAY);
		if (between == 0){
			between = 1;
		}
		fosterCare.setUpdateTime(new Date());
		this.baseMapper.updateById(fosterCare);
		PwPetSales petSales =  salesService.getOne(new QueryWrapper<PwPetSales>().eq("order_num", this.baseMapper.selectById(fosterCare.getId()).getOrderNum()).eq("type", 2).eq("source", 2).eq("source_id", fosterCare.getId()));
		if (null == petSales){
			petSales = new PwPetSales();
			this.saveSales(petSales,fosterCare,between);
		}else{
			petSales.setMoney(fosterCare.getPrice().multiply(new BigDecimal(between)));
			salesService.updateById(petSales);
		}
	}

	private void saveSales(PwPetSales petSales, PwPetFosterCare fosterCare, long between) {
		petSales.setOrderNum(fosterCare.getOrderNum());
		petSales.setType(2);
		petSales.setSource(2);
		petSales.setSourceId(fosterCare.getId());
		petSales.setMoney(fosterCare.getPrice().multiply(new BigDecimal(between)));
		petSales.setCreateTime(new Date());
		salesService.save(petSales);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(Long id) {
		PwPetFosterCare fosterCare = this.baseMapper.selectById(id);
		if (null == fosterCare){
			throw new ServiceException("记录不存在！");
		}
		fosterCare.setDeleted(1);
		this.baseMapper.updateById(fosterCare);
		salesService.remove(new QueryWrapper<PwPetSales>().eq("type",2).eq("source",2).eq("source_id",id));
	}

	@Override
	public void end(Long id) {
		PwPetFosterCare pwPetFosterCare = this.baseMapper.selectById(id);
		pwPetFosterCare.setStatus(3);
		this.baseMapper.updateById(pwPetFosterCare);
	}

	@Override
	public void uploadPic(UploadPictureDto uploadPictureDto) {
		PwPetFosterCare fosterCare = this.baseMapper.selectById(uploadPictureDto.getId());
		if (null == fosterCare){
			throw new ServiceException("记录不存在！");
		}
		fosterCare.setPicture(uploadPictureDto.getPicStr());
		this.baseMapper.updateById(fosterCare);
	}

	@Override
	public PicturePreViewModelDto preView(Long id) {
		PwPetFosterCare fosterCare = this.baseMapper.selectById(id);
		if (null == fosterCare){
			throw new ServiceException("记录不存在！");
		}
		if (StringUtils.isBlank(fosterCare.getPicture())){
			throw new ServiceException("图片不存在，请重新上传！");
		}
		String picture = fosterCare.getPicture();
		PicturePreViewModelDto modelDto = new PicturePreViewModelDto();
		List<PictureDataDto> data = new ArrayList<>();
		PictureDataDto pictureDataDto = new PictureDataDto();
		pictureDataDto.setSrc(picture);
		data.add(pictureDataDto);
		modelDto.setData(data);
		return modelDto;
	}
}
