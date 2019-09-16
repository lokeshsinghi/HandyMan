package com.example.locale_lite;

public class Rating {

    private String userPhone;
    private String spId;
    private String rateValue;

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getSpId() {
        return spId;
    }

    public void setSpId(String spId) {
        this.spId = spId;
    }

    public String getRateValue() {
        return rateValue;
    }

    public void setRateValue(String rateValue) {
        this.rateValue = rateValue;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Rating() {
    }

    private String comment;

    public Rating(String userPhone, String spId, String rateValue, String comment) {
        this.userPhone = userPhone;
        this.spId = spId;
        this.rateValue = rateValue;
        this.comment = comment;
    }


}
