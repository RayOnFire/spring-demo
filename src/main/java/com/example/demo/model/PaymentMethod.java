package com.example.demo.model;

/**
 * Created by Ray on 2017/7/4.
 */
public enum PaymentMethod {
    ALIPAY("ALIPAY"),
    PAYPAL("PayPal"),
    WECHAT("WeChat");

    private String paymentMethod;

    PaymentMethod(String method) {
        this.paymentMethod = method;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

}
