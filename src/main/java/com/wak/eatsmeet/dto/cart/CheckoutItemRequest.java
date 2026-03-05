package com.wak.eatsmeet.dto.cart;

import com.wak.eatsmeet.model.food.enums.ItemTypes;
import lombok.Data;

import java.util.List;

@Data
public class CheckoutItemRequest {
    private String uniqueId;
    private int itemId;
    private ItemTypes itemTypes;
    private double quantity;
    private double totalPrice;
    private String date;
    private String times;
    private int cartId;
    private List<Integer> curryIds;
}
