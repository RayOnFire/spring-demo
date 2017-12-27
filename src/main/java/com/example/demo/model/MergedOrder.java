package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 2017/7/15.
 */
@Entity
@Table(name = "merged_order")
public class MergedOrder extends UserOrder {

    @OneToMany(mappedBy = "mergedOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UnifiedOrder> unifiedOrders = new ArrayList<>();

    @JsonIgnore
    @NotNull
    @OneToOne(optional = false)
    private ShoppingCart cart;

    public MergedOrder() {}

    public MergedOrder(User buyer, ShoppingCart cart, double finalPrice, UserShippingInfo userShippingInfo, PaymentMethod paymentMethod) {
        super(buyer, finalPrice, userShippingInfo, paymentMethod);
        this.cart = cart;
    }

    public List<UnifiedOrder> getUnifiedOrders() {
        return unifiedOrders;
    }

    public void setUnifiedOrders(List<UnifiedOrder> unifiedOrders) {
        this.unifiedOrders = unifiedOrders;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public void setCart(ShoppingCart cart) {
        this.cart = cart;
    }
}
