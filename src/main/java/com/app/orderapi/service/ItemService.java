package com.app.orderapi.service;

import com.app.orderapi.DTO.ItemDTO;
import com.app.orderapi.entities.Item;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ItemService {

    List<ItemDTO> getAllItems();

    ItemDTO convertEntityToDTO(Item item);

    Item getItemById(Long id);

    ItemDTO createItem(Item item);

    Item updateItem(Long id,Item item);

    void deleteItem(Long id);

    void sellItem(Long id);

    boolean isItemNameUnique(String name);
}
