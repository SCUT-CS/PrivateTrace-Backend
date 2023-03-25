package cn.edu.scut.priloc.pojo;

import java.util.List;

public class Trajectory {
    private List<TimeLocationData> tlds;

    private String path;

    public Trajectory(List<TimeLocationData> tlds, String path) {
        this.tlds = tlds;
        this.path = path;
    }

    public List<TimeLocationData> getTlds() {
        return tlds;
    }

    public void setTlds(List<TimeLocationData> tlds) {
        this.tlds = tlds;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "Trajectory{" +
                "tlds=" + tlds +
                ", path='" + path + '\'' +
                '}';
    }
}
