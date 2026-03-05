package com.wak.eatsmeet.service;

import com.wak.eatsmeet.dto.ApiResponse;
import com.wak.eatsmeet.dto.cart.CartItemResponse;
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

import com.wak.eatsmeet.model.order.Orders;
import com.wak.eatsmeet.model.order.OrderItems;
import com.wak.eatsmeet.model.order.OrderStatus;
import com.wak.eatsmeet.repository.order.OrderRepo;
import com.wak.eatsmeet.repository.order.OrderItemRepo;
import com.wak.eatsmeet.dto.cart.CheckoutItemRequest;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import com.wak.eatsmeet.model.food.enums.ItemTypes;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepo cartRepo;
    private final CartItemRepo cartItemRepo;
    private final UserService userService;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public ApiResponse addToCart(CartRequest cartRequest) {
        Calendar cal = Calendar.getInstance();

        // Start of today
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();

        // End of today
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date endOfDay = cal.getTime();

        // get user_id from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User not authenticated");
        }

        // find the cart from user email
        Cart cart = cartRepo.findByUsers(userService.getUserIdByEmail(authentication.getName()))
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUsers(userService.getUserIdByEmail(authentication.getName()));
                    return cartRepo.save(newCart);
                });

        // check if the item already exists in the cart
        for (CartRequest.CurryId curryId : cartRequest.getCurry_ids()) {

            Optional<CartItems> existingItem = cartItemRepo.findByCartAndItemIdAndCurryIdAndTimesAndCreatedDateBetween(
                    cart,
                    cartRequest.getItemId(),
                    curryId.getId(),
                    cartRequest.getTimes(),
                    startOfDay,
                    endOfDay);

            if (existingItem.isPresent()) {
                throw new IllegalArgumentException("Item already exists in cart for today");
            }
        }

        for (CartRequest.CurryId curryId : cartRequest.getCurry_ids()) {
            CartItems cartItems = new CartItems();
            cartItems.setTimes(cartRequest.getTimes());
            cartItems.setCreatedDate(new Date());
            cartItems.setItemTypes(cartRequest.getItemTypes());
            cartItems.setItemId(cartRequest.getItemId());
            cartItems.setPrice(cartRequest.getPrice());
            cartItems.setQuantity(cartRequest.getQuantity());
            cartItems.setSelected(false);
            cartItems.setCart(cart);
            cartItems.setCurryId(curryId.getId());
            cartItemRepo.save(cartItems);
            System.out.println("Loop 1");
        }

        return new ApiResponse("Item added to cart successfully", null);
    }

    public List<CartItemResponse> getCartItems() {
        // get user_id from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User not authenticated");
        }

        Users user = userService.getUserIdByEmail(authentication.getName());
        Cart cart = cartRepo.findByUsers(user)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for user"));

        // return cartItemRepo.findAllByCart(cart);
        return cartItemRepo.findAllByCart(cart)
                .stream()
                .map(item -> new CartItemResponse(
                        item.getId(),
                        item.getItemId(),
                        item.getCurryId(),
                        item.getItemTypes(),
                        item.getQuantity(),
                        item.getPrice(),
                        item.getCreatedDate(),
                        item.isSelected(),
                        item.getTimes(),
                        item.getCart().getId()))
                .toList();
    }

    public ApiResponse removeCartItem(String uniqueId) {
        // get user_id from security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User not authenticated");
        }

        try {
            // uniqueId format: yyyy-MM-dd-times-itemId-cartId[-itemType]
            // wait, date has hyphens, so first 10 chars for date
            String dateStr = uniqueId.substring(0, 10);
            String[] parts = uniqueId.substring(11).split("-");
            String times = parts[0];
            int itemId = Integer.parseInt(parts[1]);
            int cartId = Integer.parseInt(parts[2]);

            ItemTypes itemType = null;
            if (parts.length > 3) {
                itemType = ItemTypes.valueOf(parts[3]);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            // Start of day
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            Date startOfDay = cal.getTime();

            // End of day
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            cal.set(Calendar.MILLISECOND, 999);
            Date endOfDay = cal.getTime();

            Users user = userService.getUserIdByEmail(authentication.getName());
            Cart cart = cartRepo.findById(cartId)
                    .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

            if (cart.getUsers().getId() != user.getId()) {
                throw new IllegalArgumentException("Unauthorized to modify this cart");
            }

            List<CartItems> itemsToDelete = cartItemRepo.findByCartAndItemIdAndTimesAndCreatedDateBetween(
                    cart, itemId, times, startOfDay, endOfDay);

            if (itemType != null) {
                ItemTypes finalItemType = itemType;
                itemsToDelete = itemsToDelete.stream()
                        .filter(item -> item.getItemTypes() == finalItemType)
                        .toList();
            }

            cartItemRepo.deleteAll(itemsToDelete);
            return new ApiResponse("Items removed successfully", null);

        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid unique id format or execution error: " + e.getMessage());
        }
    }

    public ApiResponse removeMultipleCartItems(List<String> uniqueIds) {
        if (uniqueIds == null || uniqueIds.isEmpty()) {
            throw new IllegalArgumentException("No items provided to remove");
        }

        for (String uniqueId : uniqueIds) {
            removeCartItem(uniqueId);
        }

        return new ApiResponse("All specified items removed successfully", null);
    }

    public ApiResponse checkout(List<CheckoutItemRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("No items provided for checkout");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User not authenticated");
        }
        Users user = userService.getUserIdByEmail(authentication.getName());

        double totalAmount = 0;
        List<String> uniqueIdsToRemove = new ArrayList<>();

        // Dummy inventory check logic (to match the Prompt example)
        // In a real app we'd check actual food/inventory here
        // Set an arbitrary condition for testing, e.g. item count > 100
        if (items.size() > 100) {
            Map<String, String> errMap = new HashMap<>();
            errMap.put("error", "Insufficient inventory");
            return new ApiResponse("Checkout failed", errMap);
        }

        for (CheckoutItemRequest item : items) {
            totalAmount += item.getTotalPrice();
            uniqueIdsToRemove.add(item.getUniqueId());
        }

        Orders order = new Orders();
        order.setUsers(user);
        order.setOrderDate(new Date());
        order.setUpdateDate(new Date());
        order.setTotalAmount(totalAmount);
        order.setStatus(OrderStatus.CONFIRMED);

        Orders savedOrder = orderRepo.save(order);

        for (CheckoutItemRequest item : items) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrders(savedOrder);
            orderItem.setItem_id(item.getItemId());
            orderItem.setItemTypes(item.getItemTypes());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getTotalPrice());
            orderItem.setCreated_date(new Date());
            orderItem.setSelected(true);

            // Check and set curryIds
            if (item.getCurryIds() != null && !item.getCurryIds().isEmpty()) {
                orderItem.setCurryIds(item.getCurryIds());
            }

            orderItemRepo.save(orderItem);
        }

        // Remove from cart
        this.removeMultipleCartItems(uniqueIdsToRemove);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("orderId", "ORD-" + savedOrder.getId());
        responseData.put("totalAmount", totalAmount);
        responseData.put("itemsCount", items.size());
        responseData.put("status", savedOrder.getStatus().toString());

        return new ApiResponse("Checkout completed successfully", responseData);
    }
}
