package com.example.locale_lite;

public class Customers {
    String firstname;
    String lastname;
    String emailid;
    String phonenum;
    String city;

    public Customers() {

    }

    public Customers(String firstname, String lastname, String emailid, String phonenum, String city) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailid = emailid;
        this.phonenum = phonenum;
        this.city = city;
    }


    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmailid() {
        return emailid;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getCity() {
        return city;
    }


}