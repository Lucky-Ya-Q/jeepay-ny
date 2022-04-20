package com.jeequan.jeepay.core.model.params;

import com.jeequan.jeepay.core.entity.OrderItem;
import lombok.Data;

import java.util.List;

@Data
public class OrderParams {
    private String mchNo;
    private List<OrderItem> orderItemList;
}
