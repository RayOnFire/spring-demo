package com.example.demo.aspect;

import com.example.demo.exception.UserNotMatchException;
import com.example.demo.model.CustomUserDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
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
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 2017/7/12.
 */
@Component
@Aspect
public class UserResolveAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserRepository userRepository;

    @Around("execution(* com.example.demo.controller.*.*(@com.example.demo.annotation.CurrentUser (*), ..))")
    public Object resolveAuthenticatedUser(ProceedingJoinPoint pjp) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object ret = null;
        if (authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof String) {
                String username = (String) principal;
                logger.debug(username);
                assert username.equals("anonymousUser");
                throw new UserNotMatchException("Anonymous User Not Allowed");
            } else if (principal instanceof CustomUserDetails) {
                Object[] args = pjp.getArgs();
                CustomUserDetails userDetails = (CustomUserDetails) principal;
                logger.debug(userDetails.getUsername());
                args[0] = userDetails.getUser();
                ret = pjp.proceed(args);
            }
        } else {
            logger.debug("not authenticated");
            throw new UserNotMatchException("Unauthenticated");
        }
        return ret;
    }
}
