package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ray on 2017/7/3.
 */
@Entity
public class Merchandise implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    private long jdId;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private double price;

    @NotNull
    @ManyToOne(optional = false)
    private User publisher;

    @NotNull
    @Column(nullable = false)
    private Timestamp publishDate = new Timestamp(System.currentTimeMillis());

    @Type(type = "text")
    private String description;

    private String slogan;

    @Type(type = "text")
    private String pageHtml;

    @OneToMany(mappedBy = "merchandise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CoverImage> coverImages = new ArrayList<>();

    @NotNull
    @Column(nullable = false, columnDefinition = "BIGINT UNSIGNED")
    private long inventory;

    @JsonIgnore
    @OneToMany(mappedBy = "merchandise", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MerchandiseRecommend> recommends = new ArrayList<>();

    public Merchandise() {}

    public Merchandise(String name, double price, User publisher, Timestamp publishDate, long inventory) {
        this.name = name;
        this.price = price;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.inventory = inventory;
    }

    public Merchandise(String name, double price, User publisher, Timestamp publishDate,long inventory, String description) {
        this.name = name;
        this.price = price;
        this.publisher = publisher;
        this.publishDate = publishDate;
        this.description = description;
        this.inventory = inventory;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getPublisher() {
        return publisher;
    }

    public void setPublisher(User publisher) {
        this.publisher = publisher;
    }

    public Timestamp getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Timestamp publishDate) {
        this.publishDate = publishDate;
    }

    public long getInventory() {
        return inventory;
    }

    public void setInventory(long inventory) {
        this.inventory = inventory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getJdId() {
        return jdId;
    }

    public void setJdId(long jdId) {
        this.jdId = jdId;
    }

    public List<CoverImage> getCoverImages() {
        return coverImages;
    }

    public void setCoverImages(List<CoverImage> coverImages) {
        this.coverImages = coverImages;
    }

    public String getPageHtml() {
        return pageHtml;
    }

    public void setPageHtml(String pageHtml) {
        this.pageHtml = pageHtml;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public List<MerchandiseRecommend> getRecommends() {
        return recommends;
    }

    public void setRecommends(List<MerchandiseRecommend> recommends) {
        this.recommends = recommends;
    }
    //TODO: 对不同等级的会员提供不同的折扣

    @Override
    public String toString() {
        return "Merchandise{" +
                "name='" + name + '\'' +
                '}';
    }
}
