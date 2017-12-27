package com.example.demo.aspect;

import com.example.demo.exception.*;
import com.example.demo.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Ray on 2017/7/4.
 */
@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler({InventoryNotEnoughException.class,
            UserNotMatchException.class,
            PaymentNotSupportException.class,
            UsernameNotFoundException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    ErrorMessage handleCustomException(Exception e) {
        return new ErrorMessage(e.getMessage());
    }

    @ExceptionHandler({ObjectNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    ErrorMessage handleObjectNotFoundException(Exception e) {
        return new ErrorMessage(e.getMessage());
    }
}
