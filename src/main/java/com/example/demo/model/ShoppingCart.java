package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 2017/7/3.
 */
@Entity
public class ShoppingCart {
    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    @NotNull
    @Column(nullable = false)
    private boolean isSubmit = false;

    public ShoppingCart() {}

    public ShoppingCart(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public boolean isSubmit() {
        return isSubmit;
    }

    public void setSubmit(boolean submit) {
        isSubmit = submit;
    }

    public long getTotal() {
        long total = 0;
        for (CartItem item: cartItems) {
            Merchandise merchandise = item.getMerchandise();
            total += merchandise.getPrice() * item.getAmount();
        }
        return total;
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "id=" + id +
                ", user=" + user +
                ", cartItems=" + cartItems +
                ", isSubmit=" + isSubmit +
                '}';
    }
}
