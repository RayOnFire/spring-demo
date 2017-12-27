package com.example.demo.service;

import com.example.demo.model.CartItem;
import com.example.demo.model.Merchandise;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.MerchandiseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by ray on 17-7-17.
 */
@Service
public class CartServiceImpl implements CartService {

    private MerchandiseRepository merchandiseRepository;

    private CartRepository cartRepository;

    private CartItemRepository cartItemRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public CartServiceImpl(MerchandiseRepository merchandiseRepository, CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.merchandiseRepository = merchandiseRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * Make this method transactional will affect some row to be locked.
     * Under MySQL's default isolation level: REPEATABLE_READ
     * No row will be locked. If some
     */
    @Override
    @Transactional
    public ShoppingCart addItemToUserCart(User user, long merchandiseId, long amount) {
        // findOne may cause ObjectNotFoundException, roll back for it
        // No need to throw checked exception here, merchandise is null will violate database's data integrity
        Merchandise merchandise = merchandiseRepository.findOne(merchandiseId);
        // To avoid pressure on database, application server should check the result first
        // if (merchandise == null) return null;
        ShoppingCart cart = cartRepository.findByUserAndIsSubmitFalse(user);
        if (cart == null) cart = new ShoppingCart(user);
        cart = cartRepository.save(cart);
        // if program breaks here, it doesn't matter as this cart with become the only unsubmitted cart
        CartItem cartItem = new CartItem();
        cartItem.setMerchandise(merchandise);
        cartItem.setAmount(amount);
        cartItem.setCart(cart);
        // if merchandise delete here, will cause save action break database's integrity.
        cartItemRepository.save(cartItem);
        cart.getCartItems().add(cartItem);
        logger.debug(cart.toString());
        return cart;
    }
}
