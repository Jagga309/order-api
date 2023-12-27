package com.app.orderapi.DTO;

import lombok.Data;

// PriceDTO for Displaying only the required fields which are present.
@Data
public class  PricingDTO {
    private double basePrice;
    private double discount;
    private String specialOffer;
}
