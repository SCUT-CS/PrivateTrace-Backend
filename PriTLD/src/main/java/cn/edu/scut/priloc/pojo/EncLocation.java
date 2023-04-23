package cn.edu.scut.priloc.pojo;

import Priloc.geo.Location;
import Priloc.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import sg.smu.securecom.protocol.Paillier;

import java.io.Serializable;
import java.math.BigInteger;

public class EncLocation implements Serializable {
    @JsonIgnore
    private BigInteger latitude;
    @JsonIgnore
    private BigInteger longitude;

    private String lati;

    private String longi;

    public EncLocation (Location location){
        this.latitude= Utils.encryptDouble(location.getLatitude(),8);
        this.longitude= Utils.encryptDouble(location.getLongitude(),8);
    }

    public Location deEnc(EncLocation encLocation){
        Paillier paillier = new Paillier();
        BigInteger e = new BigInteger(String.valueOf(1e8));
        double lati =paillier.decrypt(this.latitude).divide(e).doubleValue();
        double longi = paillier.decrypt(this.longitude).divide(e).doubleValue();
        return new Location(lati,longi);
    }
    public EncLocation(BigInteger latitude, BigInteger longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.lati=latitude.toString();
        this.longi=longitude.toString();
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public BigInteger getLatitude() {
        return latitude;
    }

    public void setLatitude(BigInteger latitude) {
        this.latitude = latitude;
    }

    public BigInteger getLongitude() {
        return longitude;
    }

    public void setLongitude(BigInteger longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "EncLocation{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
