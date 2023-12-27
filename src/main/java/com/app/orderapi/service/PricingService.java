package com.app.orderapi.service;

import com.app.orderapi.DTO.PricingDTO;
import com.app.orderapi.entities.Pricing;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface PricingService {

    public ResponseEntity<Pricing> addDiscountToItem(Long itemId, double discount);

    public ResponseEntity<Pricing> removeDiscountFromItem(Long itemId);

    public ResponseEntity<Pricing> updatePricing(Long itemId, PricingDTO pricingDTO);
}
