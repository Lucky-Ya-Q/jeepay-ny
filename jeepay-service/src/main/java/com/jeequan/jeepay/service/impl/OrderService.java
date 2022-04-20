package com.jeequan.jeepay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jeequan.jeepay.core.entity.*;
import com.jeequan.jeepay.core.exception.BizException;
import com.jeequan.jeepay.core.model.params.OrderParams;
import com.jeequan.jeepay.core.model.security.JeeWxUser;
import com.jeequan.jeepay.core.utils.OrderNoUtils;
import com.jeequan.jeepay.service.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author [mybatis plus generator]
 * @since 2022-04-20
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {
    @Autowired
    private SkuStockMapper skuStockMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductCategoryMapper productCategoryMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void jsapiPay(OrderParams orderParams) {
        String mchNo = orderParams.getMchNo();
        WxUser wxUser = JeeWxUser.getCurrentWxUser().getWxUser();

        Order order = new Order();
        order.setMchNo(mchNo);
        order.setMemberId(wxUser.getUserId());
        order.setMemberUsername(wxUser.getNikename());
        order.setOrderTime(new Date());
        order.setOrderSn(OrderNoUtils.getOrderNo());
        order.setStatus("待取货");
        order.setPayType("微信");
        order.setPaymentTime(new Date());
        orderMapper.insert(order);


        long payAmount = 0L;
        List<OrderItem> orderItemList = orderParams.getOrderItemList();
        for (OrderItem orderItem : orderItemList) {
            Integer goumaiNum = orderItem.getGoumaiNum();
            if (goumaiNum == 0) {
                continue;
            }

            SkuStock skuStock = skuStockMapper.selectById(orderItem.getSkuId());
            Product product = productMapper.selectById(skuStock.getProductId());
            ProductCategory productCategory = productCategoryMapper.selectById(product.getCategoryId());

            if (skuStock.getStock() < goumaiNum) {
                throw new BizException("【" + product.getName() + "】-【" + skuStock.getName() + "】库存不足");
            }
            skuStock.setStock(skuStock.getStock() - goumaiNum);
            skuStockMapper.updateById(skuStock);


            orderItem.setMchNo(mchNo);
            orderItem.setOrderId(order.getOrderId());
            orderItem.setOrderSn(order.getOrderSn());
            orderItem.setProductId(product.getProductId());
            orderItem.setName(product.getName());
            orderItem.setDescription(product.getDescription());
            orderItem.setCategoryName(productCategory.getName());
            orderItem.setPic(product.getPic());
            orderItem.setSkuId(skuStock.getSkuId());
            orderItem.setSkuName(skuStock.getName());
            orderItem.setPrice(skuStock.getPrice());
            orderItem.setDeposit(skuStock.getDeposit());
            orderItem.setGoumaiNum(goumaiNum);
            orderItemMapper.insert(orderItem);

            payAmount += (orderItem.getPrice() + orderItem.getDeposit()) * goumaiNum;
        }

        order.setPayAmount(payAmount);
        orderMapper.updateById(order);
    }
}
