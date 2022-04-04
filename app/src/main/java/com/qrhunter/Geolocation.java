package com.qrhunter;

/**
 * The Geolocation is a class that represents a latitude and longitude for QR codes and the player.
 *
 * @author Kyle
 * @author Zack
 */
public class Geolocation {
    private Double longitude;
    private Double latitude;


    /**
     * Constructs a Geolocation with explicitly provided latitude and longitude.
     * @param lng The latitude.
     * @param lat The longitude.
     * @throws IllegalArgumentException If either lat or lng are null.
     */
    public Geolocation(Double lng, Double lat) {
        if (lng == null || lat == null) throw new IllegalArgumentException();
        longitude = lng; latitude = lat;
    }


    /**
     * Returns the longitude.
     * @return The longitude.
     */
    public Double getLongitude() {return longitude;}


    /**
     * Returns the latitude.
     * @return The latitude.
     */
    public Double getLatitude() {return latitude;}


    /**
     * Sets the longitude.
     * @param lng The longitude.
     * @throws IllegalArgumentException If the provided longitude is null.
     */
    public void setLongitude(Double lng) {
        if (lng == null) throw new IllegalArgumentException();
        longitude = lng;
    }


    /**
     * Sets the latitude.
     * @param lat The latitude.
     * @throws IllegalArgumentException If the provided latitude is null.
     */
    public void setLatitude(Double lat) {
        if (lat == null) throw new IllegalArgumentException();
        latitude = lat;
    }
}
