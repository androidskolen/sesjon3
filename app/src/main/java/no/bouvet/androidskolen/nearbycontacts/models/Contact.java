package no.bouvet.androidskolen.nearbycontacts.models;

import android.graphics.Bitmap;

import com.google.gson.Gson;

public class Contact {

    private static final Gson gson = new Gson();

    private final String name;
    private final String email;
    private final String telephone;
    private Bitmap picture;

    public Contact(String name, String email, String telephone) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }

    public Contact(String name, String email, String telephone, Bitmap picture) {
        this.name = name;
        this.email = email;
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return name;
    }

    public String toJson() {
        return gson.toJson(this);
    }

    public static Contact fromJson(String json) {
        return gson.fromJson(json, Contact.class);
    }

}
