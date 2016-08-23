package com.example.macmini1_cuceimobile.horarioudeg;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

/**
 * Created by agonzalez on 18/08/2016.
 */
public class GeoLocationManager implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Context context;

    public Location getActualLocation() {
        return actualLocation;
    }

    protected Location actualLocation;

    public double getLatitude(){
        return actualLocation.getLatitude();
    }

    public double getLongitude(){
        return  actualLocation.getLongitude();
    }

    public boolean isPermissionGranted() {
        return permissionGranted;
    }

    public void setPermissionGranted(boolean permissionGranted) {
        this.permissionGranted = permissionGranted;
    }

    protected boolean permissionGranted;

    public GeoLocationManager(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        permissionGranted = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if(permissionGranted)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    public Location getLastLocation(){
        permissionGranted = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if(permissionGranted)
            return  locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        else
            return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        actualLocation = location;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
