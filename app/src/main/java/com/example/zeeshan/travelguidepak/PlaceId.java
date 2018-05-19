package com.example.zeeshan.travelguidepak;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class PlaceId {

    @Exclude
    public String PlaceId;

    public <T extends PlaceId> T withId(@NonNull final String id) {
        this.PlaceId = id;
        return (T) this;
    }

}