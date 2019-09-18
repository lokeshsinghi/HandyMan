package com.example.locale_lite;

public class RequestBox {

        private String sender;
        private String receiver;
        private String description;
        private String date;
        private String time;
        private String status;
        private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RequestBox(String sender, String receiver, String description,String date,String time,String status) {
            this.sender = sender;
            this.receiver = receiver;
            this.description = description;
            this.date = date;
            this.time = time;
            this.status = status;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public RequestBox(){

        }

}
