package com.example.bloodapp;

public class requstDisplay {

    public String getRequestBlood() {
        return requestBlood;
    }

    public void setRequestBlood(String requestBlood) {
        this.requestBlood = requestBlood;
    }

    public String getRequestContact() {
        return requestContact;
    }

    public void setRequestContact(String requestContact) {
        this.requestContact = requestContact;
    }

    public String getRequestquantity() {
        return requestquantity;
    }

    public void setRequestquantity(String requestquantity) {
        this.requestquantity = requestquantity;
    }

    public String getRequestcity() {
        return requestcity;
    }

    public void setRequestcity(String requestcity) {
        this.requestcity = requestcity;
    }

    public String getRequsthospital() {
        return requsthospital;
    }

    public void setRequsthospital(String requsthospital) {
        this.requsthospital = requsthospital;
    }

    public String requestBlood, requestContact, requestquantity, requestcity, requsthospital;

    public requstDisplay(String requestBlood, String requestContact, String requestquantity, String requestcity, String requsthospital) {
        this.requestBlood = requestBlood;
        this.requestContact = requestContact;
        this.requestquantity = requestquantity;
        this.requestcity = requestcity;
        this.requsthospital = requsthospital;
    }
}
