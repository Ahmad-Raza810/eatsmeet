package com.wak.eatsmeet.controller;

import com.wak.eatsmeet.dto.ApiResponse;
import com.wak.eatsmeet.dto.cart.CartItemResponse;
import com.wak.eatsmeet.dto.cart.CartRequest;
import com.wak.eatsmeet.model.cart.CartItems;
import com.wak.eatsmeet.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // get all cart items by user id
    @GetMapping("/items")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUB_ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getCartItems() {
        List<CartItemResponse> response = cartService.getCartItems();

        ApiResponse res = new ApiResponse("Cart items retrieved successfully", response);
        return ResponseEntity.ok(res);
    }

    @DeleteMapping("/remove/{uniqueId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUB_ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ApiResponse> removeCartItem(@PathVariable String uniqueId) {
        try {
            ApiResponse result = cartService.removeCartItem(uniqueId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/remove-multiple")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUB_ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ApiResponse> removeMultipleCartItems(
            @RequestBody com.fasterxml.jackson.databind.JsonNode payload) {
        System.out.println("mmmmmmmmmmmmm: " + payload);
        try {
            java.util.List<String> uniqueIds = new java.util.ArrayList<>();
            if (payload != null) {
                if (payload.isArray()) {
                    for (com.fasterxml.jackson.databind.JsonNode node : payload) {
                        uniqueIds.add(node.asText());
                    }
                } else if (payload.isObject()) {
                    payload.fields().forEachRemaining(entry -> {
                        com.fasterxml.jackson.databind.JsonNode node = entry.getValue();
                        if (node.isArray()) {
                            for (com.fasterxml.jackson.databind.JsonNode n : node) {
                                uniqueIds.add(n.asText());
                            }
                        }
                    });
                }
            }
            ApiResponse result = cartService.removeMultipleCartItems(uniqueIds);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/checkout")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUB_ADMIN') or hasAuthority('USER')")
    public ResponseEntity<ApiResponse> checkout(@RequestBody com.fasterxml.jackson.databind.JsonNode payload) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            java.util.List<com.wak.eatsmeet.dto.cart.CheckoutItemRequest> items = new java.util.ArrayList<>();

            if (payload != null) {
                if (payload.isArray()) {
                    for (com.fasterxml.jackson.databind.JsonNode node : payload) {
                        items.add(mapper.treeToValue(node, com.wak.eatsmeet.dto.cart.CheckoutItemRequest.class));
                    }
                } else if (payload.isObject()) {
                    if (payload.has("items") && payload.get("items").isArray()) {
                        for (com.fasterxml.jackson.databind.JsonNode node : payload.get("items")) {
                            items.add(mapper.treeToValue(node, com.wak.eatsmeet.dto.cart.CheckoutItemRequest.class));
                        }
                    } else {
                        payload.fields().forEachRemaining(entry -> {
                            com.fasterxml.jackson.databind.JsonNode node = entry.getValue();
                            if (node.isArray()) {
                                for (com.fasterxml.jackson.databind.JsonNode n : node) {
                                    try {
                                        items.add(mapper.treeToValue(n,
                                                com.wak.eatsmeet.dto.cart.CheckoutItemRequest.class));
                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                        });
                    }
                }
            }

            ApiResponse result = cartService.checkout(items);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            java.util.Map<String, String> errMap = new java.util.HashMap<>();
            errMap.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("Checkout failed", errMap));
        }
    }
}
