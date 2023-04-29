package cn.edu.scut.priloc.pojo;

import Priloc.geo.Location;
import Priloc.utils.Utils;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class TimeLocationData implements Serializable {
    private Location location;
    private Date date;

    public EncTimeLocationData encrypt(){
        return new EncTimeLocationData(this);
    }

    public  TimeLocationData(){}

    public TimeLocationData(EncTimeLocationData eTld){
        this.date = eTld.getDate();
        //对地址进行解密
        EncLocation encLocation = eTld.getEncLocation();
        BigInteger encLati = encLocation.getLatitude();
        BigInteger encLongi = encLocation.getLongitude();
        BigInteger latitude= Utils.decryptBigInteger(encLati);
        BigInteger longitude= Utils.decryptBigInteger(encLongi);
        this.location = new Location(latitude.doubleValue()/1e8,longitude.doubleValue()/1e8);
    }
    public TimeLocationData(Location location, Date date) {
        this.location = location;
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "TimeLocationData{" +
                "location=" + location +
                ", date=" + date +
                '}';
    }
}
