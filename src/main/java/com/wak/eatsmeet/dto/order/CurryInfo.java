package com.wak.eatsmeet.dto.order;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurryInfo {
    private int id;
    private String name;
    private double price;
    private String imageUrl;
}
