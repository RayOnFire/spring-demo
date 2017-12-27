package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;

/**
 * Created by Ray on 2017/7/2.
 */
public class MyUsernamePasswordFilter extends UsernamePasswordAuthenticationFilter {

    public MyUsernamePasswordFilter(AuthenticationManager authenticationManager) {
        super();
        this.setAuthenticationManager(authenticationManager);
    }
}
