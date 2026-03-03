package com.wak.eatsmeet.model.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wak.eatsmeet.model.food.enums.ItemTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private int id;

    private int itemId;

    @Column(nullable = true, name = "curry_id")
    private int curryId;

    @Enumerated(EnumType.STRING)
    private ItemTypes itemTypes;

    private double quantity;
    private double price;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Kolkata")
    @Column(name = "created_date")
    private Date createdDate;
    private boolean selected;
    private String times;

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "cart_id")
    @JsonIgnore
    private Cart cart;
}
