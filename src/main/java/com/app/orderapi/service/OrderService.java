package com.app.orderapi.service;

import com.app.orderapi.DTO.OrderDTO;
import com.app.orderapi.DTO.UserOrderDTO;
import com.app.orderapi.entities.Order;
import com.app.orderapi.enums.OrderStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

     OrderDTO createOrder(Long userId);

     OrderDTO createOrder(Long userId,Long addressId);

     OrderDTO createOrder(Long userID,Long addressId,Long itemID);

     OrderDTO updateOrderStatus(Long orderID);

     List<Order> findOrderByUserEmail(String emailID);

     String cancelOrder(Long orderId);

     OrderDTO getOrderDetailsById(Long orderId);

     List<OrderDTO> searchOrders(String query);

     List<OrderDTO> searchOrdersByOrderStatus(String status);

     UserOrderDTO getPreviousOrders(Long userId);
}
