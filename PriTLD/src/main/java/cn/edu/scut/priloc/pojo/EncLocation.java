package cn.edu.scut.priloc.pojo;

import Priloc.geo.Location;
import Priloc.utils.Utils;
import sg.smu.securecom.protocol.Paillier;

import java.math.BigInteger;

public class EncLocation {
    private BigInteger latitude;
    private BigInteger longitude;

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
