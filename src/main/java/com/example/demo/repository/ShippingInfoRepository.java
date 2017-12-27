package com.example.demo.repository;

import com.example.demo.model.User;
import com.example.demo.model.UserShippingInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Ray on 2017/7/4.
 */
public interface ShippingInfoRepository extends CrudRepository<UserShippingInfo, Long> {

    List<UserShippingInfo> findAllByUser(User user);

    UserShippingInfo findByUser(User user);
}
