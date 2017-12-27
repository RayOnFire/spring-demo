package com.example.demo.model;

import javax.persistence.*;

/**
 * Created by Ray on 2017/7/15.
 */
@Entity
public class MerchandiseRecommend {

    @Id
    @GeneratedValue
    private long id;

    @ManyToOne(optional = false)
    private Merchandise merchandise;

    @OneToOne(optional = false)
    private Merchandise recommendation;

    public MerchandiseRecommend() {
    }

    public MerchandiseRecommend(Merchandise merchandise, Merchandise recommendation) {
        this.merchandise = merchandise;
        this.recommendation = recommendation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Merchandise getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(Merchandise merchandise) {
        this.merchandise = merchandise;
    }

    public Merchandise getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Merchandise recommendation) {
        this.recommendation = recommendation;
    }
}
