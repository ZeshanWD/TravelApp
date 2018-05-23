package com.example.zeeshan.travelguidepak;

import java.util.Date;

public class Comment {

    private String message;
    private String userId;
    private Date timestamp;

    public Comment(){

    }

    public Comment(String message, String userId, Date timestamp) {
        this.message = message;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
