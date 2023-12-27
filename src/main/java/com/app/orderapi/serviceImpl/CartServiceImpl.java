package com.app.orderapi.serviceImpl;

import com.app.orderapi.entities.Cart;
import com.app.orderapi.repository.CartRepository;
import com.app.orderapi.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart getCartById(Long cartId) {
        return cartRepository.getCartById(cartId);
    }
}
