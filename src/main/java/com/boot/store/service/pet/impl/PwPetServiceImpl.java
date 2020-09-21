package com.boot.store.service.pet.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.store.entity.PwPet;
import com.boot.store.entity.PwPetSales;
import com.boot.store.exception.ServiceException;
import com.boot.store.mapper.PwPetMapper;
import com.boot.store.service.pet.IPwPetSalesService;
import com.boot.store.service.pet.IPwPetService;
import com.boot.store.utils.UUIDUtils;
import com.boot.store.vo.PageVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class PwPetServiceImpl extends ServiceImpl<PwPetMapper, PwPet> implements IPwPetService {

	@Autowired
	private IPwPetSalesService petSalesService;

	@Override
	public PageVo<PwPet> list(Integer page, Integer limit, String typeId, String name) {
		if (page == 1){
			page = 0;
		}else{
			page = limit*(page -1);
		}
		List<PwPet> dtoList = this.baseMapper.list(page,limit,name,typeId);
		Long count = this.baseMapper.count(name,typeId);
		return new PageVo<>(count,dtoList);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void addPet(PwPet pet) {
		pet.setCreateTime(new Date());
		this.baseMapper.insert(pet);
		PwPetSales petSales = new PwPetSales();
		petSales.setOrderNum(UUIDUtils.genOrder());
		petSales.setType(1);
		petSales.setSource(1);
		petSales.setSourceId(pet.getId());
		petSales.setMoney(pet.getPurchasePrice());
		petSales.setCreateTime(new Date());
		petSalesService.save(petSales);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void editPet(PwPet pet) {
		PwPet pwPet = this.baseMapper.selectById(pet.getId());
		if (pwPet.getStatus() == 3 && pet.getStatus() != 3){
			throw new ServiceException("已售出宠物不允许更改状态！");
		}
		this.baseMapper.updateById(pet);
		List<PwPetSales> petSalesList = petSalesService.list(new QueryWrapper<PwPetSales>().eq("source_id", pet.getId()));
		petSalesList.forEach(sale ->{
			if (sale.getType() == 1){
				sale.setMoney(pet.getPurchasePrice());
			}
			if (sale.getType() == 2){
				sale.setMoney(pet.getSellPrice());
			}
		});
		petSalesService.updateBatchById(petSalesList);
	}

	@Override
	public void deletePet(String id) {
		PwPet pwPet = this.baseMapper.selectById(id);
		if (null == pwPet){
			throw new ServiceException("宠物不存在！");
		}
		pwPet.setDeleted(1);
		this.updateById(pwPet);
	}

	@Override
	public List<PwPetSales> record(Long id) {
		PwPet pwPet = this.baseMapper.selectById(id);
		if (null == pwPet){
			throw new ServiceException("宠物不存在！");
		}
		List<PwPetSales> list = petSalesService.list(new QueryWrapper<PwPetSales>().eq("source", 1).eq("source_id", id));
		list.forEach(l ->{
			l.setExtName(pwPet.getName());
		});
		return list;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void sellPet(String id) {
		PwPet pwPet = this.baseMapper.selectById(Long.valueOf(id));
		if (pwPet.getStatus() == 3){
			throw new ServiceException("宠物已售出！");
		}
		if (null == pwPet){
			throw new ServiceException("宠物不存在！");
		}
		pwPet.setStatus(3);
		pwPet.setSellDate(new Date());
		this.updateById(pwPet);
		List<PwPetSales> list = petSalesService.list(new QueryWrapper<PwPetSales>().eq("type", 2).eq("source", 1).eq("source_id", pwPet.getId()));
		if (CollectionUtils.isEmpty(list)){
			PwPetSales petSales = new PwPetSales();
			petSales.setOrderNum(UUIDUtils.genOrder());
			petSales.setType(2);
			petSales.setSource(1);
			petSales.setSourceId(pwPet.getId());
			petSales.setMoney(pwPet.getSellPrice());
			petSales.setCreateTime(new Date());
			petSalesService.save(petSales);
		}
	}
}
