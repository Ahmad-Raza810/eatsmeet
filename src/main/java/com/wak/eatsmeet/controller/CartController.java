package com.wak.eatsmeet.controller;


import com.wak.eatsmeet.dto.ApiResponse;
import com.wak.eatsmeet.dto.cart.CartRequest;
import com.wak.eatsmeet.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUB_ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody CartRequest cartRequest) {
        try {
            ApiResponse result = cartService.addToCart(cartRequest);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
