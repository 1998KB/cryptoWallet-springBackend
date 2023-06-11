package com.wallet.wallet.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Users")
public class UserObj {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "username")
    private String username;

    @Column (name = "email")
    private String email;

    @Column (name = "age")
    private int age;

    @Column(name = "password")
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id", referencedColumnName = "wallet_id")
    private Wallet userWallet;

    public UserObj() {
    }

    public UserObj(Long id, String username, String email, int age, String password, Wallet userWallet) {
        Id = id;
        this.username = username;
        this.email = email;
        this.age = age;
        this.password = password;
        this.userWallet = userWallet;
    }

    public Long getId() {
        return Id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }

    public Wallet getUserWallet() {
        return userWallet;
    }

    public void setId(Long id) {
        Id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserWallet(Wallet userWallet) {
        this.userWallet = userWallet;
    }
}
