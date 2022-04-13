package com.jeequan.jeepay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

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
@TableName("r_product")
public class Product implements Serializable {

    //gw
    public static final LambdaQueryWrapper<Product> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID=1L;

    /**
     * 商品ID
     */
    @TableId(value = "product_id", type = IdType.AUTO)
    private Long productId;

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 图片
     */
    private String pic;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 上架状态：0-下架，1-上架
     */
    private Byte publishState;

    /**
     * 销量
     */
    private Integer sale;

    /**
     * 删除状态：0-未删除，1-已删除
     */
    private Byte deleteState;

    @TableField(exist = false)
    private List<SkuStock> skuStockList;
}
