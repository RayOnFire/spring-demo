package com.example.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Ray on 2017/7/2.
 */
// @service("myRememberMeService")
public class MyRememberMeService extends AbstractRememberMeServices implements RememberMeServices {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public MyRememberMeService (String key, UserDetailsService userDetailsService) {
        super(key, userDetailsService);
    }

    @Override
    protected void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication) {
        logger.info(String.format("key: %s", getKey()));
        logger.info(String.format("Cookie: %s", getCookieName()));
        logger.info(String.format("Param: %s", getParameter()));
    }

    @Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) throws RememberMeAuthenticationException, UsernameNotFoundException {
        return null;
    }
}
