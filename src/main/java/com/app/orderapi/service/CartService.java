package com.app.orderapi.service;

import com.app.orderapi.entities.Cart;
import org.springframework.stereotype.Service;

@Service
public interface CartService {

    Cart getCartById(Long cartId);

}
