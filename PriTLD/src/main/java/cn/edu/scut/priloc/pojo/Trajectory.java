package cn.edu.scut.priloc.pojo;

import java.io.Serializable;
import java.util.List;

public class Trajectory implements Serializable {
    private List<TimeLocationData> tlds;

    private String userId;


    public Trajectory(){}
    public Trajectory(List<TimeLocationData> tlds ,String userId) {
        this.tlds = tlds;
        this.userId = userId;
    }

    public List<TimeLocationData> getTlds() {
        return tlds;
    }

    public void setTlds(List<TimeLocationData> tlds) {
        this.tlds = tlds;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Trajectory{" +
                "tlds=" + tlds +
                ", userId='" + userId + '\'' +
                '}';
    }
}
