package com.borneo.ecommerce.service;

import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.User;

public interface CartService {

    Cart getCartByUser(User user);

    void addProductToCart(User user, Long productId, int quantity);

    void removeProductFromCart(User user, Long productId);

    void deleteByUser(User user);


}