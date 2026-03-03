package com.wak.eatsmeet.dto.cart;

import com.wak.eatsmeet.model.food.enums.ItemTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemResponse {
    private int id;
    private int itemId;
    private int curryId;
    private ItemTypes itemTypes;
    private double quantity;
    private double price;
    private Date createdDate;
    private boolean selected;
    private String times;
    private int cartId;
}
