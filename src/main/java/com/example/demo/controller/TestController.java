package com.example.demo.controller;

import com.example.demo.annotation.CurrentUser;
import com.example.demo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ray on 2017/7/12.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @GetMapping
    public String get(@CurrentUser User user, @RequestParam("param") String param1) {
        logger.info(param1);
        return user.getUsername();
    }
}
