package com.app.orderapi.DTO;

import com.app.orderapi.entities.Pricing;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

// ItemDTO for Displaying only the required fields which are present.
@Data
public class ItemDTO {

    private String name;
    private String description;
    private double basePrice;
    private String category;

}
