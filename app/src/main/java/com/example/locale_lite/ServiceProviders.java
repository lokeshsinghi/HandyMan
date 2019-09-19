package com.example.locale_lite;

import android.net.Uri;

import com.google.android.gms.maps.model.LatLng;

public class ServiceProviders {
    String firstname;
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

    public Boolean getPending() {
        return pending;
    }

    public void setPending(Boolean pending) {
        this.pending = pending;
    }

    Boolean pending;


    int totalrate;
    int numrate;
    float avrate;

    public int getTotalrate() {
        return totalrate;
    }

    public void setTotalrate(int totalrate) {
        this.totalrate = totalrate;
    }

    public int getNumrate() {
        return numrate;
    }

    public void setNumrate(int numrate) {
        this.numrate = numrate;
    }

    public float getAvrate() {
        return avrate;
    }

    public void setAvrate(float avrate) {
        this.avrate = avrate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String id;

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
        this.numrate =0;
        this.totalrate=0;
    }

    public ServiceProviders(String firstname, String lastname, String emailid, String phonenum, String city, String category,String profilePicUrl) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailid = emailid;
        this.phonenum = phonenum;
        this.city = city;
        this.category = category;
        this.profilePicUrl = profilePicUrl;
        this.numrate = 0;
        this.totalrate =0;
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
