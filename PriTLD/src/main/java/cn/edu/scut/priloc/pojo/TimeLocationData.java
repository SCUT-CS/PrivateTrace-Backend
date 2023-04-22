package cn.edu.scut.priloc.pojo;

import Priloc.geo.Location;

import java.io.Serializable;
import java.util.Date;

public class TimeLocationData implements Serializable {
    private Location location;
    private Date date;

    public EncTimeLocationData encrypt(){
        return new EncTimeLocationData(this);
    }

    public  TimeLocationData(){}
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
