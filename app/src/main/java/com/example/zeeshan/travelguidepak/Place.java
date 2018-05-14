package com.example.zeeshan.travelguidepak;


public class Place {
    private String city, description, image, thumbnai, title, userId;

    public Place(){

    }

    public Place(String city, String description, String image, String thumbnai, String title, String userId) {
        this.city = city;
        this.description = description;
        this.image = image;
        this.thumbnai = thumbnai;
        this.title = title;
        this.userId = userId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getThumbnai() {
        return thumbnai;
    }

    public void setThumbnai(String thumbnai) {
        this.thumbnai = thumbnai;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
