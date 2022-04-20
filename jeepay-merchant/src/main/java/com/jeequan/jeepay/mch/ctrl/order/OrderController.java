package com.jeequan.jeepay.mch.ctrl.order;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jeequan.jeepay.core.constants.ApiCodeEnum;
import com.jeequan.jeepay.core.entity.Order;
import com.jeequan.jeepay.core.entity.OrderItem;
import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.mch.ctrl.CommonCtrl;
import com.jeequan.jeepay.service.impl.OrderService;
import com.jeequan.jeepay.service.mapper.OrderItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController extends CommonCtrl {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderItemMapper orderItemMapper;

    /**
     * 订单列表
     *
     * @return
     */
    @GetMapping
    public ApiRes list() {
        Order order = getObject(Order.class);
        LambdaQueryWrapper<Order> condition = Order.gw().eq(Order::getMchNo, getCurrentMchNo())
                .like(StrUtil.isNotEmpty(order.getOrderSn()), Order::getOrderSn, order.getOrderSn());

        IPage<Order> pages = orderService.page(getIPage(), condition);
        return ApiRes.ok(pages);
    }

    /**
     * 订单详情
     *
     * @param orderId
     * @return
     */
    @GetMapping("/{orderId}")
    public ApiRes detail(@PathVariable("orderId") Long orderId) {
        Order order = orderService.getById(orderId);
        if (order == null || !order.getMchNo().equals(getCurrentMchNo())) {
            return ApiRes.fail(ApiCodeEnum.SYS_OPERATION_FAIL_SELETE);
        }
        List<OrderItem> orderItemList = orderItemMapper.selectList(OrderItem.gw().eq(OrderItem::getOrderId, orderId));
        order.setOrderItemList(orderItemList);
        return ApiRes.ok(order);
    }
}
