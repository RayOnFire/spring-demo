package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Ray on 2017/7/14.
 */
@Entity
public class CoverImage implements Serializable {

    @JsonIgnore
    @Id
    @GeneratedValue
    private long id;

    @NotNull
    @Column(nullable = false)
    private String url;

    @JsonIgnore
    @ManyToOne(optional = false)
    private Merchandise merchandise;

    public CoverImage() {
    }

    public CoverImage(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Merchandise getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(Merchandise merchandise) {
        this.merchandise = merchandise;
    }

    @Override
    public String toString() {
        return "CoverImage{" +
                "url='" + url + '\'' +
                '}';
    }
}
