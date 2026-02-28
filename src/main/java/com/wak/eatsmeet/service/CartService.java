package com.wak.eatsmeet.service;

import com.wak.eatsmeet.dto.ApiResponse;
import com.wak.eatsmeet.dto.cart.CartRequest;
import com.wak.eatsmeet.model.cart.Cart;
import com.wak.eatsmeet.model.cart.CartItems;
import com.wak.eatsmeet.model.user.Users;
import com.wak.eatsmeet.repository.cart.CartItemRepo;
import com.wak.eatsmeet.repository.cart.CartRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final UserService userService;

    public ApiResponse addToCart(CartRequest cartRequest) {
        //get user_id from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User not authenticated");
        }

        // find the cart from user email
        Cart cart = cartRepo.findByUsers(userService.getUserIdByEmail(authentication.getName()))
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUsers(userService.getUserIdByEmail(authentication.getName()));
                    return cartRepo.save(newCart);
                });



        for(CartRequest.CurryId curryId : cartRequest.getCurry_ids()) {
            CartItems cartItems = new CartItems();
            cartItems.setTimes(cartRequest.getTimes());
            cartItems.setCreated_date(new Date());
            cartItems.setItemTypes(cartRequest.getItemTypes());
            cartItems.setItemId(cartRequest.getItemId());
            cartItems.setPrice(cartRequest.getPrice());
            cartItems.setQuantity(cartRequest.getQuantity());
            cartItems.setSelected(false);
            cartItems.setCart(cart);
            cartItems.setCurry_id(curryId.getId());
            cartItemRepo.save(cartItems);
            System.out.println("Loop 1");
        }

        return new ApiResponse("Item added to cart successfully", null);
    }

    public List<CartItems> getCartItems() {
        //get user_id from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User not authenticated");
        }

        Users user = userService.getUserIdByEmail(authentication.getName());
        Cart cart = cartRepo.findByUsers(user).orElseThrow(() -> new IllegalArgumentException("Cart not found for user"));
        CartItems cartItems = new CartItems();

        return cartItemRepo.findAllByCart(cart);
    }
}
