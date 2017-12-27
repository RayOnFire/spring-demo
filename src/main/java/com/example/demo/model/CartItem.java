package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by Ray on 2017/7/10.
 */
@Entity
public class CartItem implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private Merchandise merchandise;

    @NotNull
    @Column(nullable = false)
    private long amount;

    @NotNull
    @Column(nullable = false)
    private Timestamp date = new Timestamp(System.currentTimeMillis());

    @JsonIgnore
    @ManyToOne(optional = false)
    private ShoppingCart cart;

    public long getId() {
        return id;
    }

    public CartItem() {}

    public CartItem(Merchandise merchandise, long amount, Timestamp date, ShoppingCart cart) {
        this.merchandise = merchandise;
        this.amount = amount;
        this.date = date;
        this.cart = cart;
    }

    public Merchandise getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(Merchandise merchandise) {
        this.merchandise = merchandise;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", merchandise=" + merchandise +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
