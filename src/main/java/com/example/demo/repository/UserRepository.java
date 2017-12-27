package com.example.demo.repository;

import com.example.demo.model.CartItem;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Ray on 2017/6/29.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
