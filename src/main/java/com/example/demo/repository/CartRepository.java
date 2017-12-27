package com.example.demo.repository;

import com.example.demo.model.ShoppingCart;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Ray on 2017/7/10.
 */
public interface CartRepository extends CrudRepository<ShoppingCart, Long> {

    //@Query("select s from ShoppingCart s where s.user = ?1 and s.isSubmit = false")
    ShoppingCart findByUserAndIsSubmitFalse(User user);
}
