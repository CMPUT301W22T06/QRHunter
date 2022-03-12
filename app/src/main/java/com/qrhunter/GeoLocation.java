package com.qrhunter;

import android.util.Pair;

/**
 * This class represents a geolocation in terms of
 * latitude and longitude
 * @author Zack Rodgers
 */
public class GeoLocation {
    private Double longitude;
    private Double latitude;
    public GeoLocation(Double latitude, Double longitude){
        this.longitude = longitude;
        this.latitude = latitude;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    /**
     * Returns latitude & longitude as a coordinate pair
     * @return
     */
    public Pair<Double, Double> getCoordinates() {
        Pair<Double, Double> coordinates = new Pair<Double, Double>(latitude, longitude);
        return coordinates;
    }
}

