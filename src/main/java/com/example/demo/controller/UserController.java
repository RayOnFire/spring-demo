package com.example.demo.controller;

import com.example.demo.annotation.CurrentUser;
import com.example.demo.model.User;
import com.example.demo.model.UserShippingInfo;
import com.example.demo.repository.ShippingInfoRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Ray on 2017/7/4.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShippingInfoRepository shippingInfoRepository;

    @PostMapping("/new")
    public User addUser(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        User user = new User(username, password);
        userRepository.save(user);
        return user;
    }

    @GetMapping("/shippinginfo")
    public List<UserShippingInfo> getShippingInfo(@CurrentUser User user) {
        return shippingInfoRepository.findAllByUser(user);
    }

    @PostMapping("/shippinginfo")
    public List<UserShippingInfo> addShippingInfo(@CurrentUser User user,
                                            @RequestParam("address") String address,
                                            @RequestParam("name") String name,
                                            @RequestParam("phoneNumber") String phoneNumber) {
        UserShippingInfo shippingInfo = new UserShippingInfo(user, address, phoneNumber, name);
        shippingInfoRepository.save(shippingInfo);
        return shippingInfoRepository.findAllByUser(user);
    }
}
