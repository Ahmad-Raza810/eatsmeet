package com.wak.eatsmeet.dto.cart;

import lombok.Data;
import java.util.List;

@Data
public class CheckoutRequest {
    private List<CheckoutItemRequest> items;
}
