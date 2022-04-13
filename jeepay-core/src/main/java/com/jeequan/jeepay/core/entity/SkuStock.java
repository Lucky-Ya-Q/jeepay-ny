package com.jeequan.jeepay.core.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("r_sku_stock")
public class SkuStock implements Serializable {

    //gw
    public static final LambdaQueryWrapper<SkuStock> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID=1L;

    /**
     * 规格ID
     */
    private Long skuId;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 规格名称
     */
    private String name;

    /**
     * 价格，单位分
     */
    private Long price;

    /**
     * 押金，单位分
     */
    private Long deposit;

    /**
     * 库存
     */
    private Integer stock;


}
