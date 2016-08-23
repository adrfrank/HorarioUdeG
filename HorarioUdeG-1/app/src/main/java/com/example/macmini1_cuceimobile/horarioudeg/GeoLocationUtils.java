package com.example.macmini1_cuceimobile.horarioudeg;

import android.location.Location;


/**
 * Created by agonzalez on 19/08/2016.
 */
public class GeoLocationUtils {

    public static boolean LocationInPolygon(Coordinate l, Coordinate[] polygon){
        int i;
        int j;
        boolean result = false;
        for (i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
            if ((polygon[i].getLatitude() > l.getLatitude()) != (polygon[j].getLatitude() > l.getLatitude()) &&
                    (l.getLongitude() < (polygon[j].getLongitude() - polygon[i].getLongitude()) * (l.getLatitude() - polygon[i].getLatitude()) / (polygon[j].getLatitude()-polygon[i].getLatitude()) + polygon[i].getLongitude())) {
                result = !result;
            }
        }
        return result;
    }
}
