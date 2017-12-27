package com.example.demo.service;

import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;

/**
 * Created by ray on 17-7-17.
 */
public interface CartService {

    ShoppingCart addItemToUserCart(User user, long merchandiseId, long amount);
}
