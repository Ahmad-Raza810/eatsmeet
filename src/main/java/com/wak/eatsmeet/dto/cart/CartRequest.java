package com.wak.eatsmeet.dto.cart;

import com.wak.eatsmeet.model.food.enums.ItemTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartRequest {
    private ItemTypes itemTypes;
    private int itemId; //food_id
    private  double price;
    private int quantity;
    private List<CurryId> curry_ids;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CurryId {
        private int id;
    }
}
