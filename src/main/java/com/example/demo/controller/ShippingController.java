package com.example.demo.controller;

import com.example.demo.annotation.CurrentUser;
import com.example.demo.exception.ObjectNotFoundException;
import com.example.demo.exception.UserNotMatchException;
import com.example.demo.model.SellerShippingInfo;
import com.example.demo.model.UnifiedOrder;
import com.example.demo.model.User;
import com.example.demo.repository.SellerShippingInfoRepository;
import com.example.demo.repository.UnifiedOrderRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping")
public class ShippingController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UnifiedOrderRepository unifiedOrderRepository;

    @Autowired
    private SellerShippingInfoRepository sellerShippingInfoRepository;

    @PostMapping
    public SellerShippingInfo addShippingInfo(@CurrentUser User user,
                                              @RequestParam("order_id") Long orderId,
                                              @RequestParam("vendor") String vendor,
                                              @RequestParam("note_id") String noteId)
            throws ObjectNotFoundException, UserNotMatchException {
        UnifiedOrder order = unifiedOrderRepository.findOne(orderId);
        if (order.getSeller() != user) throw new UserNotMatchException("Only seller can publish shipping info");
        SellerShippingInfo sellerShippingInfo = new SellerShippingInfo(order, vendor, noteId);
        sellerShippingInfoRepository.save(sellerShippingInfo);
        return sellerShippingInfo;
    }
}
