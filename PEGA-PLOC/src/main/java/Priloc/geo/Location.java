package Priloc.geo;

import Priloc.area.basic.EncryptedPoint;
import Priloc.utils.Turple;

import java.io.Serializable;

public class Location implements Serializable {
    private double latitude;
    private double longitude;
    private double altitude = 0.0;

    public Location(){}

    public Location(double latitude, double longitude, double altitude) {
        this(latitude, longitude);
        this.altitude = altitude;
    }

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public String toString() {
        return "PlainLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", altitude=" + altitude +
                //", XYZ=" + toXYZ() +
                '}';
    }

    public Turple<Double, Double, Double> toXYZ() {
        return Utils.gcj02ToXYZ(latitude, longitude, altitude);
    }

    public EncryptedPoint encrypt() {
        Turple<Double, Double, Double> xyz = toXYZ();
        return new EncryptedPoint(xyz.first, xyz.second, xyz.third);
    }

    public double squareDistance(Location other) {
        Turple<Double, Double, Double> xyz1 = toXYZ();
        Turple<Double, Double, Double> xyz2 = other.toXYZ();
        return Math.pow(xyz1.first - xyz2.first, 2) + Math.pow(xyz1.second - xyz2.second, 2) + Math.pow(xyz1.third - xyz2.third, 2);
    }

    public double distance(Location other) {
        return Math.sqrt(squareDistance(other));
    }
}

