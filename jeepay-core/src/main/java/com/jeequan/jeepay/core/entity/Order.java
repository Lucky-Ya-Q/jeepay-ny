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
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("r_order")
public class Order implements Serializable {

    //gw
    public static final LambdaQueryWrapper<Order> gw(){
        return new LambdaQueryWrapper<>();
    }

    private static final long serialVersionUID=1L;

    /**
     * 订单ID
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Long orderId;

    /**
     * 商户号
     */
    private String mchNo;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 会员用户名
     */
    private String memberUsername;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 订单编号
     */
    private String orderSn;

    /**
     * 应付金额，单位分
     */
    private Long payAmount;

    /**
     * 订单状态
     */
    private String status;

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 支付时间
     */
    private Date paymentTime;

    @TableField(exist = false)
    private List<OrderItem> orderItemList;
}
