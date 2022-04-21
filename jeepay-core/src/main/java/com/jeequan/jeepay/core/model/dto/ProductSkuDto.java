package com.jeequan.jeepay.core.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class ProductSkuDto {
    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 图片
     */
    private String pic;

    /**
     * 规格ID
     */
    private Long skuId;

    /**
     * 规格名称ID
     */
    private Long nameId;

    /**
     * 规格名称
     */
    private String skuName;

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
