package com.app.orderapi.controllers;

import com.app.orderapi.DTO.ItemDTO;
import com.app.orderapi.DTO.UserDTO;
import com.app.orderapi.entities.Item;
import com.app.orderapi.entities.User;
import com.app.orderapi.exceptions.ItemNotFoundException;
import com.app.orderapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String displayMessage(){
        return "Working Fine...";
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user){
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/confirm-account")
    public ResponseEntity<Boolean> confirmUserAccount(@RequestParam("token") String token){
        Boolean isSuccess = userService.verifyToken(token);
        return ResponseEntity.ok(isSuccess);
    }



    @GetMapping("{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userService.getUserById(id);
        if(user!=null){
            return new ResponseEntity<>(user, HttpStatus.OK);
        }else{
            throw new ItemNotFoundException("No Such User Exists!!!");
        }
    }
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUserById(@PathVariable Long id,@RequestBody User user){

        User updatedUser = userService.updateUser(id,user);
        if(updatedUser!=null){
            return new ResponseEntity<>(updatedUser,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/addItems/{userId}")
    public ResponseEntity<User> addItemsToUserCart(@PathVariable Long userId,@RequestParam Long itemId,
     @RequestParam(value = "quantity" , defaultValue = "1" , required = false) int quantity){
        User updatedUser = userService.addItemsToCart(userId,itemId,quantity);
        if(updatedUser!=null){
            return new ResponseEntity<>(updatedUser,HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/removeItems/{userId}")
    public ResponseEntity<Void> removeItemFromUserCart(@PathVariable Long userId,@RequestParam Long itemId){
        userService.removeItemFromCart(userId,itemId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/removeAllItems/{userId}")
    public ResponseEntity<User> removeAllItemsFromCart(@PathVariable Long userId){
        User updatedUser = userService.removeAllItemsFromCart(userId);
        return new ResponseEntity<>(updatedUser,HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam("query") String query){
        return ResponseEntity.ok(userService.searchUsers(query));
    }
}
