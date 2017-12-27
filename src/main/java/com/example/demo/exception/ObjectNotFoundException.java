package com.example.demo.exception;

/**
 * Created by ray on 17-7-16.
 */
public class ObjectNotFoundException extends Exception {

    public ObjectNotFoundException() {
        super("Null Entity");
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }
}
