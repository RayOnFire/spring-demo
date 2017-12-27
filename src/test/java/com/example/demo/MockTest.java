package com.example.demo;

import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.*;

/**
 * Created by ray on 17-12-27.
 */
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class MockTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void shouldAddToCart() throws Exception {
        MvcResult result = mockMvc.perform(post("/cart")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("id", "1")
                .param("amount", "1"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logger.info(result.getResponse().getContentAsString());
    }
}
