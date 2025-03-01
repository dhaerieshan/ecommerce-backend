package com.borneo.ecommerce.service;

import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.User;
import org.springframework.transaction.annotation.Transactional;

public interface CartService {

    Cart getCartByUser(User user);

    @Transactional
    void clearCart(User user);

    void addProductToCart(User user, Long productId, int quantity);

    void removeProductFromCart(User user, Long productId);

    void deleteByUser(User user);


}