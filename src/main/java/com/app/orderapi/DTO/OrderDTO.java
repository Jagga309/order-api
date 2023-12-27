package com.app.orderapi.DTO;

import com.app.orderapi.entities.Order;
import com.app.orderapi.enums.OrderStatus;
import com.app.orderapi.enums.PaymentType;
import lombok.Data;

// OrderDTO for Displaying only the required fields which are present.
@Data
public class OrderDTO {

    private Long orderId;
    private String userName;
    private String phoneNumber;
    private String emailId;
    private String address;
    private String item;
    private Double price;
    private OrderStatus orderStatus;
    private PaymentType paymentType;

}
