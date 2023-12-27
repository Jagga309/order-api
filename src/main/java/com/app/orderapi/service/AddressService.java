package com.app.orderapi.service;

import com.app.orderapi.entities.Address;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {

     Address addAddress(Address address,Long userId);

     Address updateAddress(Address updatedAddress,Long addressId);

     void removeAddress(Long addressId);
}
