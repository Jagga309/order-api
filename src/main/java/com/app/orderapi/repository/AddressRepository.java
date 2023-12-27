package com.app.orderapi.repository;

import com.app.orderapi.entities.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {

//    public Address findByCity(String city);
}
