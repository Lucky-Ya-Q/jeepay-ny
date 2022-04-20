package com.jeequan.jeepay.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 订单明细表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("r_order_item")
public class OrderItem implements Serializable {

    //gw
    public static final LambdaQueryWrapper<OrderItem> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID=1L;

    /**
     * 订单项ID
     */
    @TableId(value = "item_id", type = IdType.AUTO)
    private Long itemId;

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 图片
     */
    private String pic;

    /**
     * 规格ID
     */
    private Long skuId;

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
     * 购买数量
     */
    private Integer goumaiNum;


}
