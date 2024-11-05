package com.borneo.ecommerce.service;

import com.borneo.ecommerce.exception.ResourceNotFoundException;
import com.borneo.ecommerce.model.Cart;
import com.borneo.ecommerce.model.CartItem;
import com.borneo.ecommerce.model.Product;
import com.borneo.ecommerce.model.User;
import com.borneo.ecommerce.repository.CartRepository;
import com.borneo.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUser(user);
            newCart.setItems(new ArrayList<>());   
            return cartRepository.save(newCart);
        });
    }


    @Override
    @Transactional
    public void addProductToCart(User user, Long productId, int quantity) {
        Cart cart = getCartByUser(user);
        if (cart.getItems() == null) {
            cart.setItems(new ArrayList<>());
        }

        Optional<CartItem> existingCartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }

        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void removeProductFromCart(User user, Long productId) {
        Cart cart = getCartByUser(user);
        if (cart.getItems() != null) {
            cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
            cartRepository.save(cart);
        }
    }

    @Override
    @Transactional
    public void deleteByUser(User user) {
        Cart cart = getCartByUser(user);
        cartRepository.delete(cart);
    }

}