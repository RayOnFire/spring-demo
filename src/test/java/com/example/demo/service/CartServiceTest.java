package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.MerchandiseRepository;
import com.example.demo.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by ray on 17-7-17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class CartServiceTest {

    @Autowired
    private MerchandiseRepository merchandiseRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    CartService cartService;

    @Test
    public void testCartService() {
        User user = userRepository.findOne(1L);
        cartService.addItemToUserCart(user, 1L, 1L);
    }

    @Test
    public void testCartServiceFail() {
        User user = userRepository.findOne(1L);
        cartService.addItemToUserCart(user, 10001L, 1L);
    }
}
