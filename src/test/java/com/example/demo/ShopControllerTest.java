package com.example.demo;

import com.example.demo.repository.*;
import com.example.demo.service.ResponseTimeService;
import com.example.demo.model.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.BDDMockito.*;

/**
 * Created by Ray on 2017/7/3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchandiseRepository merchandiseRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    @Autowired
    private ResponseTimeService responseTimeService;

    private Merchandise testMerchandise;
    private Merchandise testMerchandise2;
    private User ray;
    private User buyer;
    private User seller;
    private UserShippingInfo testShippingInfo;

    @Before
    public void setUp() throws Exception {
        ray = new User("Ray", "pwd");
        userRepository.save(ray);
        buyer = new User("Buyer #1", "pwd");
        userRepository.save(buyer);
        seller = new User("Seller #1", "pwd");
        userRepository.save(seller);
        testMerchandise = new Merchandise("Test Merchandise #1", 599, ray, new Timestamp(System.currentTimeMillis()), 10000);
        merchandiseRepository.save(testMerchandise);
        testMerchandise2 = new Merchandise("Test Merchandise #2", 399, seller, new Timestamp(System.currentTimeMillis()), 10000);
        merchandiseRepository.save(testMerchandise2);
        testShippingInfo = new UserShippingInfo(buyer, "GZ #1", "+86 1234567", "ashdaiusdh");
        shippingInfoRepository.save(testShippingInfo);
    }

    private void logResult(MvcResult result) throws UnsupportedEncodingException {
        System.out.println();
        System.out.println(String.format("Request for %s %s", result.getRequest().getMethod(), result.getRequest().getRequestURI()));
        System.out.println(String.format("Status Code: %s", result.getResponse().getStatus()));
        System.out.println(String.format("Response: %s", result.getResponse().getContentAsString()));
        System.out.println();
    }

    @Test
    public void testTransactional() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result = mockMvc.perform(post("/cart")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("id", "1")
                .param("amount", "1"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        System.out.println(result);
        System.out.println(result.getResponse().getContentAsString());
        ShoppingCart cart = mapper.readValue(result.getResponse().getContentAsString(), ShoppingCart.class);
        System.out.println(cart);
    }

    public void testOrder() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ShoppingCart cart = new ShoppingCart(buyer);
        cartRepository.save(cart);
        CartItem cartItem = new CartItem(testMerchandise, 1, new Timestamp(System.currentTimeMillis()), cart);
        CartItem cartItem2 = new CartItem(testMerchandise2, 2, new Timestamp(System.currentTimeMillis()), cart);
        cart.getCartItems().add(cartItem);
        cart.getCartItems().add(cartItem2);
        cartRepository.save(cart);
        MvcResult result = mockMvc.perform(post("/order")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("shipinfo_id", String.valueOf(testShippingInfo.getId()))
                .param("payment_id", "1"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logResult(result);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        UnifiedOrder[] unifiedOrders = mapper.readValue(result.getResponse().getContentAsString(), UnifiedOrder[].class);
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(unifiedOrders));

        result = mockMvc.perform(post("/order/cancel/request")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("order_id", String.valueOf(unifiedOrders[0].getId())))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logResult(result);

        result = mockMvc.perform(post("/order/cancel/accept")
                .with(csrf())
                .with(user("Ray"))
                .param("order_id", String.valueOf(unifiedOrders[0].getId())))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logResult(result);
    }

    public void testShoppingCartSQL() {
        ShoppingCart cart = new ShoppingCart(ray);
        cartRepository.save(cart);
        CartItem newCartItem = new CartItem(testMerchandise, 10, new Timestamp(System.currentTimeMillis()), cart);
        cartItemRepository.save(newCartItem);
        CartItem cartItem = cartItemRepository.findOne(1L);
        //User user = cartItem.getCart().getUser();
    }

    public void testShoppingCart() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        MvcResult result;

        result = mockMvc.perform(post("/cart")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("merchandise_id", String.valueOf(testMerchandise.getId()))
                .param("amount", String.valueOf(10)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logResult(result);
        ShoppingCart cart = mapper.readValue(result.getResponse().getContentAsString(), ShoppingCart.class);

        result = mockMvc.perform(put("/cart")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("cartitem_id", String.valueOf(cart.getCartItems().get(0).getId()))
                .param("amount", String.valueOf(11)))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logResult(result);

        result = mockMvc.perform(delete("/cart")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("cartitem_id", String.valueOf(cart.getCartItems().get(0).getId())))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logResult(result);

        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/cart")
                    .with(csrf())
                    .with(user("Buyer #1"))
                    .param("merchandise_id", String.valueOf(testMerchandise.getId()))
                    .param("amount", String.valueOf(10)))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();
        }

        result = mockMvc.perform(delete("/cart")
                .with(csrf())
                .with(user("Buyer #1")))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logResult(result);

        result = mockMvc.perform(get("/cart")
                .with(csrf())
                .with(user("Buyer #1")))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logResult(result);
    }


    // @WithMockUser(username = "Ray", password = "pwd", authorities = "ROLE_ADMIN")
    public void testShop() throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        System.out.println(System.currentTimeMillis());

        MvcResult result;

        result = mockMvc.perform(post("/shop/merchandise")
                .with(csrf())
                .with(user("Ray"))
                .param("item_name", "ps4")
                .param("item_price", "499")
                .param("inventory", "0"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("ps4")))
                .andReturn();
        logResult(result);
        Merchandise merchandise = mapper.readValue(result.getResponse().getContentAsString(), Merchandise.class);
        //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(merchandise));

        mockMvc.perform(get("/shop/merchandises"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("ps4")))
                .andReturn();

        mockMvc.perform(get("/shop/merchandises")
                .param("publisher", String.valueOf(ray.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("ps4")))
                .andReturn();


        mockMvc.perform(get("/shop/merchandises")
                .param("publisher", "999"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(containsString("user not found")))
                .andReturn();

        mockMvc.perform(get("/shop/merchandise")
                .param("item_id", String.valueOf(merchandise.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("ps4")))
                .andReturn();

        mockMvc.perform(get("/shop/merchandise")
                .param("item_id", "999"))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string(containsString("No such a merchandise")))
                .andReturn();

        result = mockMvc.perform(post("/user/shippinginfo")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("address", "1600 Amphitheatre Pkwy, Mountain View, CA 94043")
                .param("phone_number", "+1 650-253-0000"))
                .andReturn();
        logResult(result);
        UserShippingInfo shippingInfo = mapper.readValue(result.getResponse().getContentAsString(), UserShippingInfo.class);
        //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(shippingInfo));

        // Test for inventory not enough
        /*
        result = mockMvc.perform(post("/shop/order")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("item_id", String.valueOf(merchandise.getId()))
                .param("item_price", String.valueOf(merchandise.getPrice()))
                .param("shipinfo_id", String.valueOf(shippingInfo.getId())))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("{\"message\":\"Inventory is not enough\"}"))
                .andReturn();
        logResult(result);
        */
        //UserOrder userOrder = mapper.readValue(result.getResponse().getContentAsString(), UserOrder.class);
        //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userOrder));

        // Update inventory
        result = mockMvc.perform(put("/shop/merchandise")
                .with(csrf())
                .with(user("Ray"))
                .param("item_id", String.valueOf(merchandise.getId()))
                .param("inventory", "10001"))
                .andReturn();
        logResult(result);

        // Concurrent test for decrease inventory
        /*
        IntStream.range(0, 1000).parallel().forEach(i -> {
            try {
                mockMvc.perform(post("/shop/order")
                        .with(csrf())
                        .with(user("Buyer #1"))
                        .param("item_id", String.valueOf(merchandise.getId()))
                        .param("item_price", String.valueOf(merchandise.getPrice()))
                        .param("shipinfo_id", String.valueOf(shippingInfo.getId())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        responseTimeService.printInfo();
        */

        List<Merchandise> merchandiseList = new ArrayList<>();
        IntStream.range(0, 100).forEach(i -> {
            try {
                MvcResult mockResult = mockMvc.perform(post("/shop/merchandise")
                        .with(csrf())
                        .with(user("Ray"))
                        .param("item_name", "Mock Item #" + String.valueOf(i))
                        .param("item_price", "499")
                        .param("inventory", "1000"))
                        .andExpect(status().is2xxSuccessful())
                        .andReturn();
                Merchandise mockMerchandise = mapper.readValue(mockResult.getResponse().getContentAsString(), Merchandise.class);
                merchandiseList.add(mockMerchandise);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        for (int j = 0; j < 2; j++) {
            responseTimeService.clear();
            ExecutorService executor = Executors.newFixedThreadPool(100);
            for (int i = 0; i < 100; i++) {
                executor.submit(() -> {
                    try {
                        mockMvc.perform(post("/shop/order")
                                .with(csrf())
                                .with(user("Buyer #1"))
                                .param("item_id", String.valueOf(merchandise.getId()))
                                .param("item_price", String.valueOf(merchandise.getPrice()))
                                .param("shipinfo_id", String.valueOf(shippingInfo.getId())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            executor.shutdown();
            executor.awaitTermination(60, TimeUnit.SECONDS);
            responseTimeService.printInfo();
        }

        responseTimeService.clear();
        ExecutorService executor2 = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executor2.submit(new MerchandiseTestRunnable(i, merchandiseList.get(i % 10)));
        }
        executor2.shutdown();
        executor2.awaitTermination(60, TimeUnit.SECONDS);
        responseTimeService.printInfo();

        responseTimeService.clear();
        ExecutorService executor3 = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executor3.submit(new MerchandiseTestRunnable(i, merchandiseList.get(i % 100)));
        }
        executor3.shutdown();
        executor3.awaitTermination(60, TimeUnit.SECONDS);
        responseTimeService.printInfo();

        result = mockMvc.perform(post("/shop/order")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("item_id", String.valueOf(merchandise.getId()))
                .param("item_price", String.valueOf(merchandise.getPrice()))
                .param("shipinfo_id", String.valueOf(shippingInfo.getId())))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        logResult(result);
        UserOrder userOrder = mapper.readValue(result.getResponse().getContentAsString(), UserOrder.class);
        //System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(userOrder));

        result = mockMvc.perform(get("/shop/merchandise")
                .param("item_id", String.valueOf(merchandise.getId())))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("ps4")))
                .andReturn();
        Merchandise newMerchandise = mapper.readValue(result.getResponse().getContentAsString(), Merchandise.class);
        //System.out.println(String.format("Inventory: %d", newMerchandise.getInventory()));

        result = mockMvc.perform(delete("/shop/order")
                .with(csrf())
                .with(user("Ray"))
                .param("order_id", String.valueOf(userOrder.getId())))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("{\"message\":\"Only buyer can delete order\"}"))
                .andReturn();
        logResult(result);

        result = mockMvc.perform(delete("/shop/order")
                .with(csrf())
                .with(user("Buyer #1"))
                .param("order_id", String.valueOf(userOrder.getId())))
                .andExpect(status().is5xxServerError())
                .andExpect(content().string("{\"message\":\"Could not delete order cause by transaction exists\"}"))
                .andReturn();
        logResult(result);


        /*
        result = mockMvc.perform(get("/shop/merchandise").with(user("Ray"))).andReturn();
        logResult(result);

        result = mockMvc.perform(get("/shop/transaction").with(user("Ray"))).andReturn();
        logResult(result);
        System.out.println(System.currentTimeMillis());
        */
    }

    class MerchandiseTestRunnable implements Runnable {
        private int id;
        private Merchandise merchandise;

        public MerchandiseTestRunnable(int id, Merchandise merchandise) {
            this.id = id;
            this.merchandise = merchandise;
        }

        @Override
        public void run() {
            try {
                mockMvc.perform(post("/shop/order")
                        .with(csrf())
                        .with(user("Buyer #1"))
                        .param("item_id", String.valueOf(merchandise.getId()))
                        .param("item_price", String.valueOf(merchandise.getPrice()))
                        .param("shipinfo_id", String.valueOf(1)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
