package com.example.demo.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Ray on 2017/7/3.
 */
public class Authority implements GrantedAuthority {
    @Override
    public String getAuthority() {
        return "ROLE_ADMIN";
    }
}
