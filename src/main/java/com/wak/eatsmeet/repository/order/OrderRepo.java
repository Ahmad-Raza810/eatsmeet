package com.wak.eatsmeet.repository.order;

import com.wak.eatsmeet.model.order.Orders;
import com.wak.eatsmeet.model.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Orders, Integer> {
    List<Orders> findByUsers(Users user);
}
