package com.boot.store.dto.commodity;

import com.boot.store.dto.auth.ValidationGroups;
import com.boot.store.dto.plugin.XmSelectModelDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author auto
 * @since 2020-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CommodityDto implements Serializable {

    private static final long serialVersionUID = 1L;

	@NotNull(message = "商品ID不能为空",groups = {ValidationGroups.Editer.class})
	private Long id;

    /**
     * 商品编号
     */
	@NotNull(message = "商品编号不能为空",groups = {ValidationGroups.Editer.class})
	private String commodityNum;

    /**
     * 商品名
     */
	@NotNull(message = "商品名不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private String name;

    /**
     * 商品图
     */
    private String picture;

    /**
     * 商品数量
     */
	@NotNull(message = "商品数量不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private Integer count;

    /**
     * 进价
     */
	@NotNull(message = "进价不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private BigDecimal purchasePrice;

    /**
     * 售价
     */
	@NotNull(message = "售价不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private BigDecimal sellPrice;

    /**
     * 供应商ID
     */
	@NotNull(message = "供应商不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private Long channelId;

    /**
     * 商品类型ID
     */
	@NotNull(message = "商品类型不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private Integer commodityTypeId;

    /**
     * 0下架 1上架
     */
	@NotNull(message = "商品状态不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private Integer status;

    /**
     * 0不打折 1打折
     */
	@NotNull(message = "商品是否折扣不能为空",groups = {ValidationGroups.Register.class,ValidationGroups.Editer.class})
	private Integer discount;

    /**
     * 折扣率
     */
    private BigDecimal discountRate;

    /**
     * 是否促销热门商品 0否 1是
     */
    private Integer promotion;

    /**
     * 备注
     */
    private String remark;

    private Date createTime;

    private Date updateTime;

    private String channelName;

    private String commodityTypeName;

    private List<XmSelectModelDto> typeList;

	private List<XmSelectModelDto> channelList;


}
