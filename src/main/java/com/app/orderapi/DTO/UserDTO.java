package com.app.orderapi.DTO;

import lombok.Data;

// UserDTO for Displaying only the required fields which are present.
@Data
public class UserDTO {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailId;
}
