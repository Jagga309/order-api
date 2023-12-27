package com.app.orderapi.serviceImpl;

import com.app.orderapi.DTO.PricingDTO;
import com.app.orderapi.entities.Item;
import com.app.orderapi.entities.Pricing;
import com.app.orderapi.exceptions.ItemNotFoundException;
import com.app.orderapi.exceptions.PricingNotFoundException;
import com.app.orderapi.repository.ItemRepository;
import com.app.orderapi.repository.PricingRepository;
import com.app.orderapi.service.PricingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PricingServiceImpl implements PricingService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PricingRepository pricingRepository;

    @Override
    public ResponseEntity<Pricing> addDiscountToItem(Long itemId, double discount) {
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new ItemNotFoundException("Item Not Found"));

        Pricing pricing = item.getPricing();
        if(pricing == null){
            pricing  = new Pricing();
            pricing.setItem(item);
        }
        pricing.setDiscount(discount);
        return ResponseEntity.ok(pricingRepository.save(pricing));
    }

    @Override
    public ResponseEntity<Pricing> removeDiscountFromItem(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow(()-> new ItemNotFoundException("Item Not Found"));
        Pricing pricing = item.getPricing();
        if(pricing != null){
            pricing.setDiscount(0.0);
            pricingRepository.save(pricing);
        }else{
            throw new PricingNotFoundException("Pricing not found for the Item...");
        }
        return ResponseEntity.ok(pricing);
    }

    @Override
    public ResponseEntity<Pricing> updatePricing(Long itemId, PricingDTO pricingDTO) {

        Item item = itemRepository.findById(itemId).orElseThrow(()-> new ItemNotFoundException("Item not found..."));

        Pricing pricing = item.getPricing();
        if(pricing == null){
            pricing = new Pricing();
            pricing.setItem(item);
        }

        pricing.setBasePrice(pricingDTO.getBasePrice());
        pricing.setDiscount(pricingDTO.getDiscount());
        pricing.setSpecialOffer(pricingDTO.getSpecialOffer());

        return ResponseEntity.ok(pricingRepository.save(pricing));
    }
}
