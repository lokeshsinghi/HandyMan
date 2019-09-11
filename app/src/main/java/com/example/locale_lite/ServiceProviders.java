package com.example.locale_lite;

public class ServiceProviders {
    String firstname;
    String lastname;
    String emailid;
    String phonenum;
    String city;
    String password;
    String cpassowrd;
    String category;

    ServiceProviders()  {

    }

    public ServiceProviders(String firstname, String lastname, String emailid, String phonenum, String city, String password, String cpassowrd, String category) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailid = emailid;
        this.phonenum = phonenum;
        this.city = city;
        this.password = password;
        this.cpassowrd = cpassowrd;
        this.category = category;
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

    public String getPassword() {
        return password;
    }

    public String getCpassowrd() {
        return cpassowrd;
    }

    public String getCategory() {
        return category;
    }
}
