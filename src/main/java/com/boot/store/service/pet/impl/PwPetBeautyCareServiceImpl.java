package com.boot.store.service.pet.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.PwPetBeautyCare;
import com.boot.store.entity.PwPetSales;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.PwPetBeautyCareMapper;
import com.boot.store.service.pet.IPwPetBeautyCareService;
import com.boot.store.service.pet.IPwPetSalesService;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author auto
 * @since 2020-09-17
 */
@Service
public class PwPetBeautyCareServiceImpl extends ServiceImpl<PwPetBeautyCareMapper, PwPetBeautyCare> implements IPwPetBeautyCareService {

	@Autowired
	private IPwPetSalesService salesService;

	@Override
	public PageVo<PwPetBeautyCare> list(Integer page, Integer limit, String type, String name) {
		QueryWrapper<PwPetBeautyCare> queryWrapper = new QueryWrapper<PwPetBeautyCare>().eq("deleted","0");
		if (StringUtils.isNotBlank(type)){
			queryWrapper.eq("pet_type",type);
		}
		if (StringUtils.isNotBlank(name)){
			queryWrapper.like("project_name",name);
		}
		IPage<PwPetBeautyCare> resultPage = this.page(new Page<>(page,limit),queryWrapper);
		return new PageVo<>(resultPage.getTotal(),resultPage.getRecords());
	}

	@Override
	public void add(PwPetBeautyCare pwPetBeautyCare) {
		this.baseMapper.insert(pwPetBeautyCare);
	}

	@Override
	public void edit(PwPetBeautyCare pwPetBeautyCare) {
		this.baseMapper.updateById(pwPetBeautyCare);
	}

	@Override
	public void delete(Long id) {
		PwPetBeautyCare pwPetBeautyCare = this.baseMapper.selectById(id);
		if (null == pwPetBeautyCare){
			throw new ServiceException("记录不存在!");
		}
		pwPetBeautyCare.setDeleted(1);
		this.baseMapper.updateById(pwPetBeautyCare);
	}

	@Override
	public void sell(Long id) {
		PwPetBeautyCare beautyCare = this.baseMapper.selectById(id);
		if (null == beautyCare){
			throw new ServiceException("服务项目不存在！");
		}
		PwPetSales petSales = new PwPetSales();
		petSales.setOrderNum(UUIDUtils.genOrder());
		petSales.setType(2);
		petSales.setSource(3);
		petSales.setSourceId(id);
		petSales.setMoney(beautyCare.getMoney());
		petSales.setCreateTime(new Date());
		salesService.save(petSales);
	}

	@Override
	public PageVo<PwPetSales> record(Long id, Integer page, Integer limit, String date) throws ParseException {
		PwPetBeautyCare pwPetBeautyCare = this.baseMapper.selectById(id);
		if (null == pwPetBeautyCare){
			throw new ServiceException("服务项目不存在！");
		}
		PageVo<PwPetSales> pwPetSalesPageVo = salesService.recordBeauty(id, page, limit, date);
		pwPetSalesPageVo.getResult().forEach(vo ->{
			vo.setExtName(pwPetBeautyCare.getProjectName());
		});
		return pwPetSalesPageVo;
	}
}
