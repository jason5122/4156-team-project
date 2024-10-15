package dev.coms4156.project.teamproject.model;

import java.io.Serializable;

public class Location implements Serializable {
    public double latitude;
    public double longitude;
    private static final int EARTH_RADIUS_KM = 6378;

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the distance between two Locations.
     */
    public double distance(Location other) {
        double dLat = Math.toRadians(latitude - other.latitude);
        double dLong = Math.toRadians(longitude - other.longitude);

        double dStartLat = Math.toRadians(latitude);
        double dEndLat = Math.toRadians(other.latitude);

        double a = haversine(dLat) + Math.cos(dStartLat) * Math.cos(dEndLat) * haversine(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    private static double haversine(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
