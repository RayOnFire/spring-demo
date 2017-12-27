package com.example.demo.aspect;

import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.service.ResponseTimeService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

/**
 * Created by Ray on 2017/6/30.
 */
//@Component
//@Aspect
public class TestAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResponseTimeService responseTimeService;

    @Before("execution(* com.example.demo.controller.*.*(..))")
    public void before () {
        /*
        LocalTime localTime = LocalTime.now();
        logger.info(String.format("Before execution: %d", localTime.toNanoOfDay() / 1000));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.info(String.format("%b", authentication.isAuthenticated()));
        logger.info(String.format("%s", userDetails.getUsername()));
        */
        //timeSpend = System.currentTimeMillis();
    }

    @Around("execution(* com.example.demo.controller.ShopController.addOrder(..))")
    public Object around (ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ret = pjp.proceed();
        long timeSpend = System.currentTimeMillis() - startTime;
        addToResponseTime(timeSpend);
        return ret;
    }

    /*
    @Around("execution(* com.example.demo.controller.ShopController.*(..))")
    public Object allAround (ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ret = pjp.proceed();
        long timeSpend = System.currentTimeMillis() - startTime;
        System.out.println("Response Time " + String.valueOf(timeSpend) + "ms");
        return ret;
    }
    */

    private synchronized void addToResponseTime(long time) {
        responseTimeService.add(time);
    }
}
