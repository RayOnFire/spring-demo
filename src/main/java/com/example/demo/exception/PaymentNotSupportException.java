package com.example.demo.exception;

/**
 * Created by Ray on 2017/7/5.
 */
public class PaymentNotSupportException extends Exception {
    public PaymentNotSupportException(String message) {
        super(message);
    }
}
