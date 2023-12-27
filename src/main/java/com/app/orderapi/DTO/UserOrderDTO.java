package com.app.orderapi.DTO;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// Combining the user entity fields as well as the order entity fields
// inorder to display the User's Orders
@Data
public class UserOrderDTO {

    private String userName;
    private String phoneNumber;
    private String emailId;
    // Using a list to store because there can be many order's associated with one user.
    private List<OrderDetailsDTO> orderDetailsDTOList = new ArrayList<>();
}

