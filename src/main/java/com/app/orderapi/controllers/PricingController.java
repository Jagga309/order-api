package com.app.orderapi.controllers;

import com.app.orderapi.DTO.PricingDTO;
import com.app.orderapi.entities.Pricing;
import com.app.orderapi.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pricing")
public class PricingController {

    @Autowired
    private PricingService pricingService;

    @PostMapping("/item/{itemId}/addDiscount")
    public ResponseEntity<Pricing> addDiscountToItem(@PathVariable Long itemId, @RequestParam double discount){
        return pricingService.addDiscountToItem(itemId,discount);
    }

    @PostMapping("/item/{itemId}/removeDiscount")
    public ResponseEntity<Pricing> removeDiscountFromItem(@PathVariable Long itemId){
        return pricingService.removeDiscountFromItem(itemId);
    }

    @PutMapping("item/{itemId}/updatePricing")
    public ResponseEntity<Pricing> updatePricing(@PathVariable Long itemId, @RequestBody PricingDTO pricingDTO){
        return pricingService.updatePricing(itemId,pricingDTO);
    }


}
