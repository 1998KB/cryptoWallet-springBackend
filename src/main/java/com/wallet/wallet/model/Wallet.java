package com.wallet.wallet.model;

import jakarta.persistence.*;
import org.springframework.stereotype.Repository;

@Entity
@Table(name = "Wallet")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long id;

    @Column(name = "address")
    private String address;
    @Column(name = "wallet_value")
    private double walletValue;

    @Column(name = "bitcoins")
    private double bitcoins;

    @Column(name = "bitcoin_value")
    private double bitcoinValue;

    public Wallet(Wallet newWallet) {
    }

    public Wallet() {

    }

    public Wallet(Long id, String address, double walletValue, double bitcoins, double bitcoinValue) {
        this.id = id;
        this.address = address;
        this.walletValue = walletValue;
        this.bitcoins = bitcoins;
        this.bitcoinValue = bitcoinValue;
    }

    public Long getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public double getWalletValue() {
        return walletValue;
    }

    public double getBitcoins() {
        return bitcoins;
    }

    public double getBitcoinValue() {
        return bitcoinValue;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setWalletValue(double walletValue) {
        this.walletValue = walletValue;
    }

    public void setBitcoins(double bitcoins) {
        this.bitcoins = bitcoins;
    }

    public void setBitcoinValue(double bitcoinValue) {
        this.bitcoinValue = bitcoinValue;
    }
}
