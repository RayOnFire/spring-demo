package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Ray on 2017/7/4.
 */
@Entity
public class UserShippingInfo implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @ManyToOne(optional = false)
    private User user;

    @NotNull
    @Column(nullable = false)
    private String address;

    @NotNull
    @Column(nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(nullable = false)
    private String name;

    public UserShippingInfo() {}

    public UserShippingInfo(User user, String address, String phoneNumber, String name) {
        this.user = user;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
