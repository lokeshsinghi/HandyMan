package com.example.locale_lite;

import android.net.Uri;

public class ServiceProviders {
    private String firstname;
    String lastname;
    String emailid;
    String phonenum;
    String city;
    String category;

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getIdProofUrl() {
        return idProofUrl;
    }

    public void setIdProofUrl(String idProofUrl) {
        this.idProofUrl = idProofUrl;
    }

    String profilePicUrl;
    String idProofUrl;



    ServiceProviders()  {

    }

    public ServiceProviders(String firstname, String lastname, String emailid, String phonenum, String city, String category) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailid = emailid;
        this.phonenum = phonenum;
        this.city = city;
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


    public String getCategory() {
        return category;
    }
}
