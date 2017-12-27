package com.example.demo.controller;

import com.example.demo.annotation.CurrentUser;
import com.example.demo.exception.InventoryNotEnoughException;
import com.example.demo.exception.ObjectNotFoundException;
import com.example.demo.exception.UserNotMatchException;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 2017/7/11.
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MerchandiseRepository merchandiseRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UnifiedOrderRepository unifiedOrderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping("/list/buyer")
    public List<UserOrder> getBuyerOrders(@CurrentUser User user,
                                    @RequestParam(value = "limit", defaultValue = "20") int limit,
                                    @RequestParam(value = "page", defaultValue = "1") int page) {
        List<UserOrder> userOrderList = new ArrayList<>();
        unifiedOrderRepository.findAllByBuyer(user,
                new PageRequest(page - 1, limit)).forEach(userOrderList::add);
        return userOrderList;
    }

    @GetMapping("/list/seller")
    public List<UserOrder> getSellerOrders(@CurrentUser User user,
                                     @RequestParam(value = "limit", defaultValue = "20") int limit,
                                     @RequestParam(value = "page", defaultValue = "1") int page) {
        List<UserOrder> userOrderList = new ArrayList<>();
        unifiedOrderRepository.findAllBySeller(user,
                new PageRequest(page - 1, limit)).forEach(userOrderList::add);
        return userOrderList;
    }

    @GetMapping
    public UserOrder getOrder(@CurrentUser User user,
                              @RequestParam("orderId") Long orderId)
            throws ObjectNotFoundException, UserNotMatchException {
        UserOrder order = orderRepository.findOne(orderId);
        if (order.getBuyer() != user) throw new UserNotMatchException("This order did not belong to you");
        return order;
    }

    @PostMapping
    public MergedOrder addOrder(@CurrentUser User buyer,
                              @RequestParam("cartId") long cartId,
                              @RequestParam("shippingInfo") long shippingInfoId,
                              @RequestParam("paymentMethod") String paymentMethod)
            throws InventoryNotEnoughException, UserNotMatchException, ObjectNotFoundException {
        return orderService.makeOrder(buyer, cartId, shippingInfoId, paymentMethod);
    }

    @PostMapping("/cancel/request")
    public UserOrder requestCancelOrder(@CurrentUser User user,
                                        @RequestParam("orderId") long orderId,
                                        @RequestParam(value = "cancel_reason", required = false) String cancelReason)
            throws ObjectNotFoundException, UserNotMatchException {
        UnifiedOrder order = unifiedOrderRepository.findOne(orderId);
        if (order.getBuyer() != user) throw new UserNotMatchException("Only buyer can delete order");
        order.setCancelled(true);
        if (cancelReason != null) order.setCancelledReason(cancelReason);
        unifiedOrderRepository.save(order);
        return order;
    }

    @PostMapping("/cancel/accept")
    public UserOrder acceptCancelOrder(@CurrentUser User user,
                                       @RequestParam("orderId") long orderId)
            throws ObjectNotFoundException, UserNotMatchException {
        UnifiedOrder order = unifiedOrderRepository.findOne(orderId);
        if (order.getSeller() != user) throw new UserNotMatchException("Only seller can accept request");
        order.setCancelAccept(true);
        unifiedOrderRepository.save(order);
        return order;
    }
}
