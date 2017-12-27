package com.example.demo.model;

import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by Ray on 2017/7/11.
 */
@Entity
public class SellerShippingInfo {

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @OneToOne(optional = false)
    private UnifiedOrder order;

    @NotNull
    @Column(nullable = false)
    private String vendor;

    @NotNull
    @Column(nullable = false)
    private String noteId;

    public SellerShippingInfo() {}

    public SellerShippingInfo(UnifiedOrder order, String vendor, String noteId) {
        this.order = order;
        this.vendor = vendor;
        this.noteId = noteId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UnifiedOrder getOrder() {
        return order;
    }

    public void setOrder(UnifiedOrder order) {
        this.order = order;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
