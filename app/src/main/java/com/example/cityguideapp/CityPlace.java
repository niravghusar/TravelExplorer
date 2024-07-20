package com.example.cityguideapp;

public class CityPlace {
    public String name;
    public String imageUrl;
    public String address;
    public String directionUrl;

    public CityPlace(String name, String address, String imageUrl, String directionUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.address = address;
        this.directionUrl = directionUrl;
    }
}
