package com.app.orderapi.repository;

import com.app.orderapi.entities.Cart;
import com.app.orderapi.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
//    void removeAllCartItems(Cart cart);
//    void deleteAllById(List<CartItem> cartItemList);
}
