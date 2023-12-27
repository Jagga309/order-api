package com.app.orderapi.repository;

import com.app.orderapi.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface  ItemRepository extends JpaRepository<Item,Long>{
    List<Item> findByName(String name);
}
