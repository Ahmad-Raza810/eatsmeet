package com.wak.eatsmeet.service;

import com.wak.eatsmeet.dto.EmployeeResponse;
import com.wak.eatsmeet.dto.UserResponse;
import com.wak.eatsmeet.model.user.Employees;
import com.wak.eatsmeet.model.user.Users;
import com.wak.eatsmeet.repository.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    private UserResponse mapToResponse(Users users) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(users.getId());
        userResponse.setName(users.getName());
        userResponse.setNic(users.getNic());
        userResponse.setEmail(users.getEmail());
        userResponse.setContact(users.getContact());
        userResponse.setRole(users.getRole());
        userResponse.setActive(users.getActive());
        userResponse.setImg_url(users.getImg_url());
        userResponse.setCart(users.getCart());
        userResponse.setOrders(users.getOrders());
        return userResponse;
    }

    public Users updateUser(int userId, Users users) {
        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found for this id: " + userId));

        user.setName(users.getName());
        user.setNic(users.getNic());
        user.setContact(users.getContact());
        user.setImg_url(users.getImg_url());
        user.setEmail(users.getEmail());
        user.setActive(users.getActive());

        return userRepo.save(user);
    }

    public List<UserResponse> getAllUsers() {
        List<UserResponse> userResponse = new ArrayList<>();
        List<Users> user = userRepo.findAll();
        user.forEach(employees1 -> {
            userResponse.add(mapToResponse(employees1));
        });

        return userResponse;
    }

    public Users getUserIdByEmail(String name) {
        Users users = userRepo.findByEmail(name)
                .orElseThrow(() -> new IllegalArgumentException("User not found for this email: " + name));
        return users;
    }

//    public Users getUserId(int userId) {
//    }
}
