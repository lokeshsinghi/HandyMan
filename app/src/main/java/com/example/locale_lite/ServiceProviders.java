package com.example.locale_lite;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class ServiceProviders {
    private String firstname;
    String lastname;
    String emailid;
    String phonenum;
    String city;
    String category;
    Float yearsOfExperience;
    String qualifications;
    Double latitude;
    Double longitude;
    String profilePicUrl;
    String idProofUrl;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }


    public float getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(float yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }



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

    public ServiceProviders(String firstname, String lastname, String emailid, String phonenum, String city, String category,String profilePicUrl) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailid = emailid;
        this.phonenum = phonenum;
        this.city = city;
        this.category = category;
        this.profilePicUrl = profilePicUrl;
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
