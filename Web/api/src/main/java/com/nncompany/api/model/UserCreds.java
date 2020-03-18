package com.nncompany.api.model;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "usercreds")
public class UserCreds{

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "login")
    String login;

    @Column(name = "pass")
    String pass;

    @OneToOne()
    @JoinColumn(name = "user_fk")
    User user;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
