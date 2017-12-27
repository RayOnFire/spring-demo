package com.example.demo.controller;

import com.example.demo.model.PaymentMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Ray on 2017/7/14.
 */
@RestController
@RequestMapping("/payment")
public class PaymentMethodController {

    @GetMapping("/method")
    public PaymentMethod[] getPaymentMethod() {
        return PaymentMethod.values();
    }
}
