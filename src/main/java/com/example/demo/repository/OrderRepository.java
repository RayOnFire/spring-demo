package com.example.demo.repository;

import com.example.demo.model.UserOrder;
import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Ray on 2017/7/4.
 */
public interface OrderRepository extends CrudRepository<UserOrder, Long> {

    //@Query("select t from UserOrder t where t.merchandise.publisher = ?1")
    Page<UserOrder> findAllByBuyer(User buyer, Pageable pageable);

    //List<UserOrder> findAllByMerchandise_Publisher(User seller);
}
