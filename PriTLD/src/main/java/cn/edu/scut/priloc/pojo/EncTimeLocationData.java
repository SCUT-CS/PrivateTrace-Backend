package cn.edu.scut.priloc.pojo;

import Priloc.area.basic.EncryptedPoint;
import Priloc.area.basic.Point;
import Priloc.geo.Location;
import Priloc.utils.Utils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class EncTimeLocationData implements Serializable {
    private static final long serialVersionUID =-48231748532854370L;
    private EncLocation encLocation;
    @JsonIgnore
    private EncryptedPoint encPoint;
    private Date date;

    public EncTimeLocationData(TimeLocationData tld) {

        this.date = tld.getDate();
        //对地址进行加密
        Location location = tld.getLocation();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        BigInteger encLati= Utils.encryptDouble(latitude);
        BigInteger encLongi= Utils.encryptDouble(longitude);
        this.encLocation = new EncLocation(encLati,encLongi);
        //生成xyz
        this.encPoint = new EncryptedPoint(new Point(location));
    }

    public TimeLocationData decrypt(){
        return new TimeLocationData(this);
    }

    public EncLocation getEncLocation() {
        return encLocation;
    }

    public void setEncLocation(EncLocation encLocation) {
        this.encLocation = encLocation;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EncryptedPoint getEncPoint() {
        return encPoint;
    }

    public void setEncPoint(EncryptedPoint encPoint) {
        this.encPoint = encPoint;
    }

    @Override
    public String toString() {
        return "EncTimeLocationData{" +
                "encLocation=" + encLocation +
                ", encPoint=" + encPoint +
                ", date=" + date +
                '}';
    }
}
