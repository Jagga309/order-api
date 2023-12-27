package com.app.orderapi.service;

import com.app.orderapi.entities.Cart;
import com.app.orderapi.entities.CartItem;
import org.springframework.stereotype.Service;

@Service
public interface CartItemService {

    void addCartItem(CartItem cartItem);

    void removeCartItem(Long cartItemId);

    void removeAllCartItems(Cart cart);

}
