package com.app.orderapi.repository;

import com.app.orderapi.DTO.OrderDTO;
import com.app.orderapi.entities.Order;
import com.app.orderapi.entities.User;
import com.app.orderapi.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query(value = "SELECT * FROM orders o WHERE o.user_id = ?1",nativeQuery = true)
    List<Order> searchOrders(Long userID);

    @Query(value = "SELECT * FROM orders o WHERE o.order_status = ?1",nativeQuery = true)
    List<Order> searchOrderByStatus(String status);

    @Query(value = "SELECT * FROM orders o WHERE o.user_id = ?1 AND o.order_status ='ORDER_CLOSED' ",nativeQuery = true)
    List<Order> getPreviousOrders(Long userId);
}
