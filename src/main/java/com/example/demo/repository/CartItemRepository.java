package com.example.demo.repository;

import com.example.demo.model.CartItem;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Ray on 2017/7/10.
 */
public interface CartItemRepository extends CrudRepository<CartItem, Long> {
}
