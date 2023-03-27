package cn.edu.scut.priloc.pojo;

import Priloc.geo.Location;
import Priloc.utils.Utils;

import java.util.Date;

public class EncTimeLocationData {
    private Location location;
    private Date date;

    public EncTimeLocationData(TimeLocationData tld) {
        this.date = tld.getDate();
        //对地址进行加密
        double encLati= Utils.encryptDouble(tld.getLocation().getLatitude(),8).longValue();
        double encLongi= Utils.encryptDouble(tld.getLocation().getLongitude(),8).longValue();
        this.location = new Location(encLati,encLongi);
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
        return "EncTimeLocationData{" +
                "location=" + location +
                ", date=" + date +
                '}';
    }
}
