package com.example.demo.service;

import com.example.demo.exception.InventoryNotEnoughException;
import com.example.demo.exception.ObjectNotFoundException;
import com.example.demo.exception.UserNotMatchException;
import com.example.demo.model.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Ray on 2017/7/5.
 */
public interface OrderService {
    MergedOrder makeOrder(User buyer, long cartId, long userShippingInfoId, String paymentMethod)
            throws InventoryNotEnoughException, UserNotMatchException, ObjectNotFoundException;
}
