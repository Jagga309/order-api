package com.app.orderapi.controllers;

import com.app.orderapi.DTO.ItemDTO;
import com.app.orderapi.entities.Item;
import com.app.orderapi.exceptions.ItemNotFoundException;
import com.app.orderapi.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping("/")
    public String displayMessage(){
        System.out.println("working Fine.....");
        return "Working Fine.....";
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemDTO>> getAllItems(){
        List<ItemDTO> items = itemService.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id){
        Item item = itemService.getItemById(id);
        if(item!=null){
           return new ResponseEntity<>(item,HttpStatus.OK);
        }else{
            throw new ItemNotFoundException("No Such Item Exists!!!");
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ItemDTO> createItem(@RequestBody Item item){
        return  ResponseEntity.ok(itemService.createItem(item));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item item){

        Item updatedItem = itemService.updateItem(id,item);
        if(updatedItem!=null){
            return new ResponseEntity<>(updatedItem,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id){
        itemService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping ("/{id}/sell")
    public ResponseEntity<Void> sellItem(@PathVariable Long id){
        itemService.sellItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
