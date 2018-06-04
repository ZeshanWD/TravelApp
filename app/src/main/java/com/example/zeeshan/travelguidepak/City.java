package com.example.zeeshan.travelguidepak;

import java.io.Serializable;

public class City implements Serializable {
    private String name;
    private String image;
    private String description;
    private Double latitude, longitude;

    public City(){

    }


    public City(String name, String image, String description, Double latitude, Double longitude) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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
}
