package com.example.demo.exception;

/**
 * Created by Ray on 2017/7/4.
 */
public class InventoryNotEnoughException extends Exception {
    public InventoryNotEnoughException() {
        super("Inventory Not Enough");
    }

    public InventoryNotEnoughException(String message) {
        super(message);
    }
}
