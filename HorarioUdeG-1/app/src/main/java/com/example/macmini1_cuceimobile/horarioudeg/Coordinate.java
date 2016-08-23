package com.example.macmini1_cuceimobile.horarioudeg;

/**
 * Created by agonzalez on 19/08/2016.
 */
public class Coordinate {
    double latitude,longitude;

    public Coordinate(double lat, double lon){
        latitude = lat;
        longitude = lon;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
