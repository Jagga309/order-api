package com.app.orderapi.serviceImpl;

import com.app.orderapi.DTO.ItemDTO;
import com.app.orderapi.entities.Item;
import com.app.orderapi.entities.Pricing;
import com.app.orderapi.exceptions.DuplicateItemException;
import com.app.orderapi.exceptions.ItemNotFoundException;
import com.app.orderapi.repository.ItemRepository;
import com.app.orderapi.repository.PricingRepository;
import com.app.orderapi.service.ItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private  PricingRepository pricingRepository;

    @Override
    public List<ItemDTO> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        return optionalItem.orElse(null);
    }

    @Override
    public ItemDTO convertEntityToDTO(Item item){
        ItemDTO itemDTO = new ItemDTO();

        itemDTO.setName(item.getName());
        itemDTO.setCategory(item.getCategory());
        itemDTO.setDescription(item.getDescription());
        itemDTO.setBasePrice(item.getPricing().getBasePrice());

        return itemDTO;
    }

    @Override
    public ItemDTO createItem(Item newItem) {
        if(isItemNameUnique((newItem.getName()))) {
            throw new DuplicateItemException("Item With Same Name already Exists!!!");
        }
        else{
            if(newItem.getPricing()!=null){
                ItemDTO itemDTO = convertEntityToDTO(newItem);

                Pricing pricing = newItem.getPricing();

                pricing.setItem(newItem);

                itemRepository.save(newItem);
                return itemDTO;
            }
        }return null;
    }

    @Override
    public Item updateItem(Long id, Item updatedItem) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(()->new ItemNotFoundException("Item not found Inorder to Update"));

        existingItem.setName(updatedItem.getName());
        existingItem.setDescription(updatedItem.getDescription());
        existingItem.setCategory(updatedItem.getCategory());
        existingItem.setAvailabiltyStatus(updatedItem.getAvailabiltyStatus());

        if(updatedItem.getPricing() != null){
            Pricing updatedPricing = updatedItem.getPricing();

            if(existingItem.getPricing() == null){
                existingItem.setPricing(new Pricing());
            }
            Pricing existingPricing = existingItem.getPricing();
            existingPricing.setBasePrice(updatedItem.getPricing().getBasePrice());
            existingPricing.setDiscount(updatedItem.getPricing().getDiscount());
            existingPricing.setSpecialOffer(updatedItem.getPricing().getSpecialOffer());
        }
        return itemRepository.save(existingItem);
    }

    @Override
    public void deleteItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if(optionalItem.isPresent()){
             itemRepository.deleteById(id);
        }else{
            throw new ItemNotFoundException("No Such Item Exists!!! Inorder to Delete...");
        }
    }

    @Override
    public void sellItem(Long id) {
        Optional<Item> optionalItem = itemRepository.findById(id);
        if(optionalItem.isPresent()){
            Item item = optionalItem.get();
            int totalItems = item.getAvailabiltyStatus();

            if(totalItems > 0) {
                item.setAvailabiltyStatus(totalItems - 1);
                itemRepository.save(item);
            }else {
                throw new ItemNotFoundException("Item is no longer available..." +
                        "Currently Out of Stock!!!");

            }
        }
    }

    @Override
    public boolean isItemNameUnique(String name) {

        List<Item> items = itemRepository.findAll();
        String nameWithoutSpaces = name.replaceAll("\\s","").toLowerCase();

        return items.stream()
                .map(Item :: getName)
                .map(existingName -> existingName.replaceAll("\\s","").toLowerCase())
                .anyMatch(itr -> itr.compareToIgnoreCase(nameWithoutSpaces)==0);
    }
}
