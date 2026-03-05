package com.wak.eatsmeet.dto.order;

import com.wak.eatsmeet.model.order.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private int orderId;
    private double totalAmount;
    private OrderStatus status;
    private Date orderDate;
    private List<OrderItemResponse> items;
}
