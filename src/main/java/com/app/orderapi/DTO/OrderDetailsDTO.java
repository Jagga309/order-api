package com.app.orderapi.DTO;

import com.app.orderapi.entities.Address;
import lombok.Data;

// Combining some fields which are required to view in an User's point of view.
@Data
public class OrderDetailsDTO {

    private String itemName;
    private int quantity;
    private double totalPrice;
    private String address;

}
