package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Ray on 2017/7/4.
 */
@Entity
public class UserOrder implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @ManyToOne(optional = false)
    private User buyer;

    @NotNull
    @Column(nullable = false)
    private Timestamp createdTime = new Timestamp(System.currentTimeMillis());

    @NotNull
    @Column(nullable = false)
    private double finalPrice;

    @NotNull
    @Column(nullable = false)
    private double freight = 0;

    @NotNull
    @ManyToOne(optional = false)
    private UserShippingInfo userShippingInfo;

    @NotNull
    @Column(nullable = false)
    private boolean isOnShipping = false;

    private Timestamp shippingTerm;

    @NotNull
    @Column(nullable = false)
    private boolean isCancelled = false;

    @NotNull
    @Column(nullable = false)
    private boolean isCancelAccept = false;

    private String cancelledReason;

    private String buyerMessage;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    public UserOrder() {}

    public UserOrder(User buyer, double finalPrice, UserShippingInfo userShippingInfo, PaymentMethod paymentMethod) {
        this.buyer = buyer;
        this.finalPrice = finalPrice;
        this.userShippingInfo = userShippingInfo;
        this.paymentMethod = paymentMethod;
    }

    public long getId() {
        return id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public double getFreight() {
        return freight;
    }

    public void setFreight(double freight) {
        this.freight = freight;
    }

    public boolean isOnShipping() {
        return isOnShipping;
    }

    public void setOnShipping(boolean onShipping) {
        isOnShipping = onShipping;
    }

    public Timestamp getShippingTerm() {
        return shippingTerm;
    }

    public void setShippingTerm(Timestamp shippingTerm) {
        this.shippingTerm = shippingTerm;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public String getCancelledReason() {
        return cancelledReason;
    }

    public void setCancelledReason(String cancelledReason) {
        this.cancelledReason = cancelledReason;
    }

    public String getBuyerMessage() {
        return buyerMessage;
    }

    public void setBuyerMessage(String buyerMessage) {
        this.buyerMessage = buyerMessage;
    }

    public UserShippingInfo getUserShippingInfo() {
        return userShippingInfo;
    }

    public void setUserShippingInfo(UserShippingInfo userShippingInfo) {
        this.userShippingInfo = userShippingInfo;
    }

    public boolean isCancelAccept() {
        return isCancelAccept;
    }

    public void setCancelAccept(boolean cancelAccept) {
        isCancelAccept = cancelAccept;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}