package com.example.demo.controller;

import com.example.demo.annotation.CurrentUser;
import com.example.demo.exception.ObjectNotFoundException;
import com.example.demo.exception.UserNotMatchException;
import com.example.demo.model.CartItem;
import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.MerchandiseRepository;
import com.example.demo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Ray on 2017/7/10.
 */
@RestController
@RequestMapping("/cart")
public class CartController {

    private MerchandiseRepository merchandiseRepository;

    private CartRepository cartRepository;

    private CartItemRepository cartItemRepository;

    private CartService cartService;

    @Autowired
    public CartController(MerchandiseRepository merchandiseRepository, CartRepository cartRepository, CartItemRepository cartItemRepository, CartService cartService) {
        this.merchandiseRepository = merchandiseRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
    }

    @PostMapping
    public ShoppingCart addToCart(@CurrentUser User user,
                          @RequestParam("id") Long merchandiseId,
                          @RequestParam("amount") Long amount)
            throws ObjectNotFoundException {
        return cartService.addItemToUserCart(user, merchandiseId, amount);
    }

    @GetMapping
    public ShoppingCart getCart(@CurrentUser User user) {
        ShoppingCart cart = cartRepository.findByUserAndIsSubmitFalse(user);
        if (cart == null) cart = new ShoppingCart(user);
        // if other thread insert a new cart with submit is false, there are two cart under submitted
        cartRepository.save(cart);
        return cart;
    }

    @PutMapping
    public ShoppingCart modifyCartItem(@CurrentUser User user,
                                       @RequestParam("id") Long cartItemId,
                                       @RequestParam("amount") Long amount)
            throws UserNotMatchException, ObjectNotFoundException {
        // TODO: realtime-checked inventory when user change amount in cart items
        CartItem cartItem = cartItemRepository.findOne(cartItemId);
        if (cartItem.getCart().getUser() != user) throw new UserNotMatchException("cart item did not belong to you");
        cartItem.setAmount(amount);
        cartItemRepository.save(cartItem);
        return cartItem.getCart();
    }

    @DeleteMapping
    public String deleteCartItem(@CurrentUser User user,
                                 @RequestParam(value = "id", required = false) Long cartItemId)
            throws ObjectNotFoundException, UserNotMatchException {
        if (cartItemId == null) {
            // delete all cart item
            ShoppingCart cart = cartRepository.findByUserAndIsSubmitFalse(user);
            List<CartItem> cartItems = cart.getCartItems();
            System.out.println(cartItems);
            cartItems.clear();
            cartRepository.save(cart);
            //cartItemRepository.delete(cartItems);
        } else {
            CartItem cartItem = cartItemRepository.findOne(cartItemId);
            if (cartItem.getCart().getUser() != user)
                throw new UserNotMatchException("cart item did not belong to you");
            cartItemRepository.delete(cartItem);
        }
        return "success";
    }
}
