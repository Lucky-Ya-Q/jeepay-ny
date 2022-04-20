package com.jeequan.jeepay.pay.ctrl.order;

import com.jeequan.jeepay.core.model.ApiRes;
import com.jeequan.jeepay.core.model.params.OrderParams;
import com.jeequan.jeepay.pay.ctrl.CommonCtrl;
import com.jeequan.jeepay.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController extends CommonCtrl {
    @Autowired
    private OrderService orderService;

    @PostMapping("/jsapiPay")
    public ApiRes jsapiPay(@RequestBody OrderParams orderParams) {
        orderService.jsapiPay(orderParams);
        return ApiRes.ok();
    }
}
