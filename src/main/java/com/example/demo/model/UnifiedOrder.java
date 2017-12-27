package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Ray on 2017/7/11.
 */
@Entity
@Table(name = "unified_order")
public class UnifiedOrder extends UserOrder {

    // TODO: Not null constraint should be add here
    @NotNull
    @OneToOne
    private User seller;

    // TODO: Not null constraint should be add here
    @JsonIgnore
    @ManyToOne
    private MergedOrder mergedOrder;

    @NotNull
    @ManyToOne(optional = false)
    private ShoppingCart cart;

    public UnifiedOrder() {}

    public UnifiedOrder(User buyer, double finalPrice, UserShippingInfo userShippingInfo, PaymentMethod paymentMethod, User seller, MergedOrder mergedOrder, ShoppingCart cart) {
        super(buyer, finalPrice, userShippingInfo, paymentMethod);
        this.seller = seller;
        this.mergedOrder = mergedOrder;
        this.cart = cart;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public MergedOrder getMergedOrder() {
        return mergedOrder;
    }

    public void setMergedOrder(MergedOrder mergedOrder) {
        this.mergedOrder = mergedOrder;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
}
