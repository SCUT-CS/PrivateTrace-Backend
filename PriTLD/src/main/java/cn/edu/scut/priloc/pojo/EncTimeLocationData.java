package cn.edu.scut.priloc.pojo;

import Priloc.geo.Location;

import java.util.Date;

public class EncTimeLocationData {
    private Location location;
    private Date date;

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
        return "Trajectory{" +
                "location=" + location +
                ", date=" + date +
                '}';
    }
}
