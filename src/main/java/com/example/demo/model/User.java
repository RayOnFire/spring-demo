package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Ray on 2017/6/29.
 */
@Entity
@Table(name = "USER", indexes = {
        @Index(columnList = "USERNAME", name = "USERNAME_INDEX")
})
public class User implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @NotEmpty
    @Column(name="USERNAME", unique=true, nullable=false)
    private String username;

    @JsonIgnore
    @NotEmpty
    @Column(name="PASSWORD", nullable=false)
    private String password;

    private String description;

    /*
    @JsonIgnore
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Blog> blogs = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    private List<User> followings = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(mappedBy = "followings")
    private List<User> followers = new ArrayList<>();
    */

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (!username.equals(user.username)) return false;
        if (!password.equals(user.password)) return false;
        return description != null ? description.equals(user.description) : user.description == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
