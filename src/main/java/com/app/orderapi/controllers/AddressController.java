package com.app.orderapi.controllers;

import com.app.orderapi.entities.Address;
import com.app.orderapi.entities.User;
import com.app.orderapi.repository.UserRepository;
import com.app.orderapi.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String display(){
        return "Working Fine from Address...";
    }

    @PostMapping("/{userId}")
    public ResponseEntity<User> addAddressToUser(@RequestBody Address address, @PathVariable Long userId){
        User user = userRepository.findById(userId).get();
        addressService.addAddress(address,userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long addressId,@RequestBody Address address){
        return new ResponseEntity<>(addressService.updateAddress(address,addressId),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeAddress(@PathVariable Long id){
        addressService.removeAddress(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
