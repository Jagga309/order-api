package com.app.orderapi.controllers;

import com.app.orderapi.DTO.OrderDTO;
import com.app.orderapi.DTO.UserOrderDTO;
import com.app.orderapi.entities.Order;
import com.app.orderapi.enums.OrderStatus;
import com.app.orderapi.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/")
    public String Display(){
        return "Working Fine From Orders.....";
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderDetailsById(@PathVariable Long orderId){
        return new ResponseEntity<>(orderService.getOrderDetailsById(orderId),HttpStatus.OK);
    }

    @PostMapping("/{userId}")
    public ResponseEntity<OrderDTO> createOrder
            (@PathVariable Long userId,
            @RequestParam(value = "addressId",defaultValue = "0",required = false) Long addressId,
            @RequestParam(value = "itemId",defaultValue = "0",required = false) Long itemID)
    {
        if(addressId != 0 && itemID != 0){
            return new ResponseEntity<>(orderService.createOrder(userId,addressId,itemID),
                    HttpStatus.OK);
        }
        else{
            if(addressId!= 0){
                return new ResponseEntity<>(orderService.createOrder(userId,addressId),HttpStatus.OK);
            }
            return new ResponseEntity<>(orderService.createOrder(userId),HttpStatus.OK);
        }
    }
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long orderId){
        return new ResponseEntity<>(orderService.updateOrderStatus(orderId),HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable Long orderId){
        return new ResponseEntity<>(orderService.cancelOrder(orderId),HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderDTO>> searchOrderByUserName(@RequestParam("query") String query){
        return  ok(orderService.searchOrders(query));
    }
    @GetMapping("/status")
    public ResponseEntity<List<OrderDTO>> searchOrderByStatus(@RequestParam String query) {
        System.out.println("Entered....");
        System.out.println(query);
        return ResponseEntity.ok(orderService.searchOrdersByOrderStatus(query));
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<UserOrderDTO> getPreviousOrders(@PathVariable Long userId) {
        return new ResponseEntity<>(orderService.getPreviousOrders(userId), HttpStatus.OK);
    }
}
