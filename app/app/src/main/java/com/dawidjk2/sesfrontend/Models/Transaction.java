package com.dawidjk2.sesfrontend.Models;

import com.dawidjk2.sesfrontend.Models.Card;

import java.io.Serializable;

public class Transaction implements Serializable {

    private Card card;
    private String vendor;
    private String amount;
    private String charity;

    public Transaction(Card of, String vendor, String amount, String charity) {
        this.card = of;
        this.vendor = vendor;
        this.amount = amount;
        this.charity = charity;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCharity() { return charity; }

    public void setCharity(String charity) { this.charity = charity; }
}
