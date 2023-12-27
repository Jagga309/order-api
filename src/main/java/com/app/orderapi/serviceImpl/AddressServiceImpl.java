package com.app.orderapi.serviceImpl;

import com.app.orderapi.entities.Address;
import com.app.orderapi.entities.User;
import com.app.orderapi.exceptions.AddressNotFoundException;
import com.app.orderapi.exceptions.UserNotFoundException;
import com.app.orderapi.repository.AddressRepository;
import com.app.orderapi.repository.UserRepository;
import com.app.orderapi.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Address addAddress(Address address, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(()->new UserNotFoundException("User doesn't exist..."));

        if(!uniqueAddressOrNot(address,userId)){
            address.setUser(user);
            user.addAddressToUser(address);
            userRepository.save(user);
            return address;
        }else return null;
    }

    @Override
    public Address updateAddress(Address updatedAddress, Long addressId) {
       Address existingAddress = addressRepository.findById(addressId)
               .orElseThrow(()->new AddressNotFoundException("Address Not Exists..."));

       existingAddress.setStreetNumber(updatedAddress.getStreetNumber());
       existingAddress.setCity(updatedAddress.getCity());
       existingAddress.setState(updatedAddress.getState());
       existingAddress.setPinCode(updatedAddress.getPinCode());
       existingAddress.setCountry(updatedAddress.getCountry());

       addressRepository.save(existingAddress);
       return existingAddress;
    }

    @Override
    public void removeAddress(Long addressId) {

        Address address = addressRepository.findById(addressId).get();
        User user = address.getUser();

        List<Address> addressList = user.getAddressList();

        addressList.remove(address);

        user.setAddressList(addressList);
        userRepository.save(user);
        addressRepository.deleteById(addressId);
    }

    public Boolean uniqueAddressOrNot(Address address,Long userId){

        List<Address> addressList = userRepository.findById(userId).get().getAddressList();
        return addressList.stream()
                .map(Address :: getStreetNumber)
                .anyMatch(itr -> itr.compareToIgnoreCase(address.getStreetNumber())==0);
    }

}
