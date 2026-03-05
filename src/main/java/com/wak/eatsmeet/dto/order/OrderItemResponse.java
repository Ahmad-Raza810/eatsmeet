package com.wak.eatsmeet.dto.order;

import com.wak.eatsmeet.model.food.enums.ItemTypes;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private int itemId;
    private ItemTypes itemType;
    private String itemName;
    private String itemImageUrl;
    private double quantity;
    private double price;
    private List<CurryInfo> curries;
}
