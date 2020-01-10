package com.dawidjk2.sesfrontend.Models;

public class Charity implements Comparable {
    public String charityName = "";
    public String websiteURL = "";
    public String generalCause = "";
    public String mission = "";
    public String tagline = "";
    public int rating = 0;

    @Override
    public int compareTo(Object o) {
        Charity charity = (Charity) o;
        if (rating > charity.rating) {
            return -1;
        } else if (rating < charity.rating) {
            return 1;
        }

        return 0;
    }
}
