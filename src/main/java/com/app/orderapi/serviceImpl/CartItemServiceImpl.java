package com.app.orderapi.serviceImpl;

import com.app.orderapi.entities.Cart;
import com.app.orderapi.entities.CartItem;
import com.app.orderapi.repository.CartItemRepository;
import com.app.orderapi.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public void addCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    @Override
    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    public void removeAllCartItems(Cart cart) {
        cartItemRepository.deleteAll(cart.getCartItemList());
    }
}
