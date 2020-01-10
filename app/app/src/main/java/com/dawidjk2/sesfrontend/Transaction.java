package com.dawidjk2.sesfrontend;

import java.io.Serializable;

public class Transaction implements Serializable {

    private Card card;
    private String vendor;
    private double amount;

    public Transaction(Card of, String vendor, double amount) {
        this.card = of;
        this.vendor = vendor;
        this.amount = amount;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
