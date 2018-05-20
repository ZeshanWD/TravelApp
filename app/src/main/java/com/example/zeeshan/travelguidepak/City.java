package com.example.zeeshan.travelguidepak;

import java.io.Serializable;

public class City implements Serializable {
    private String name;
    private String image;

    public City(){

    }

    public City(String image, String name) {
        this.image = image;
        this.name = name;
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
}
