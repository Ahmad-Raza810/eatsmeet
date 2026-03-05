package com.wak.eatsmeet.service;

import com.wak.eatsmeet.dto.ApiResponse;
import com.wak.eatsmeet.dto.order.CurryInfo;
import com.wak.eatsmeet.dto.order.OrderItemResponse;
import com.wak.eatsmeet.dto.order.OrderResponse;
import com.wak.eatsmeet.model.food.Curry;
import com.wak.eatsmeet.model.food.Foods;
import com.wak.eatsmeet.model.food.Snacks;
import com.wak.eatsmeet.model.food.Bites;
import com.wak.eatsmeet.model.order.OrderItems;
import com.wak.eatsmeet.model.order.Orders;
import com.wak.eatsmeet.model.user.Users;
import com.wak.eatsmeet.repository.food.CurryRepo;
import com.wak.eatsmeet.repository.food.FoodRepo;
import com.wak.eatsmeet.repository.food.SnackRepo;
import com.wak.eatsmeet.repository.food.BiteRepo;
import com.wak.eatsmeet.repository.order.OrderRepo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final UserService userService;
    private final CurryRepo curryRepo;
    private final FoodRepo foodRepo;
    private final SnackRepo snackRepo;
    private final BiteRepo biteRepo;

    public OrderService(OrderRepo orderRepo, UserService userService, CurryRepo curryRepo, FoodRepo foodRepo,
            SnackRepo snackRepo, BiteRepo biteRepo) {
        this.orderRepo = orderRepo;
        this.userService = userService;
        this.curryRepo = curryRepo;
        this.foodRepo = foodRepo;
        this.snackRepo = snackRepo;
        this.biteRepo = biteRepo;
    }

    public ApiResponse getUserOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("User not authenticated");
        }
        Users user = userService.getUserIdByEmail(authentication.getName());
        return fetchOrdersForUser(user);
    }

    @Transactional
    private ApiResponse fetchOrdersForUser(Users user) {
        List<Orders> orders = orderRepo.findByUsers(user);
        if (orders == null || orders.isEmpty()) {
            return new ApiResponse("No orders found", new ArrayList<>());
        }

        List<OrderResponse> responseList = new ArrayList<>();

        for (Orders order : orders) {
            List<OrderItemResponse> itemResponses = new ArrayList<>();
            for (OrderItems item : order.getOrderItems()) {
                String itemName = "Unknown Item";
                String itemImageUrl = "";

                if (item.getItemTypes() != null) {
                    switch (item.getItemTypes()) {
                        case FOODS:
                            Foods f = foodRepo.findById(item.getItem_id()).orElse(null);
                            if (f != null) {
                                itemName = f.getName();
                                itemImageUrl = f.getImg_url();
                            }
                            break;
                        case SNACKS:
                            Snacks s = snackRepo.findById(item.getItem_id()).orElse(null);
                            if (s != null) {
                                itemName = s.getName();
                                itemImageUrl = s.getImg_url();
                            }
                            break;
                        case BYTES:
                            Bites b = biteRepo.findById(item.getItem_id()).orElse(null);
                            if (b != null) {
                                itemName = b.getName();
                                itemImageUrl = b.getImg_url();
                            }
                            break;
                    }
                }
                List<CurryInfo> curryInfos = new ArrayList<>();
                if (item.getCurryIds() != null && !item.getCurryIds().isEmpty()) {
                    for (Integer curryId : item.getCurryIds()) {
                        Curry curry = curryRepo.findById(curryId).orElse(null);
                        if (curry != null) {
                            curryInfos.add(new CurryInfo(
                                    curry.getId(),
                                    curry.getName(),
                                    curry.getPrice(),
                                    curry.getImageUrl()));
                        }
                    }
                }
                itemResponses.add(new OrderItemResponse(
                        item.getItem_id(),
                        item.getItemTypes(),
                        itemName,
                        itemImageUrl,
                        item.getQuantity(),
                        item.getPrice(),
                        curryInfos));
            }

            responseList.add(new OrderResponse(
                    order.getId(),
                    order.getTotalAmount(),
                    order.getStatus(),
                    order.getOrderDate(),
                    itemResponses));
        }

        return new ApiResponse("Orders retrieved successfully", responseList);
    }
}
