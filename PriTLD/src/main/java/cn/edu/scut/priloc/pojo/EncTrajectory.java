package cn.edu.scut.priloc.pojo;


import java.util.ArrayList;
import java.util.List;

public class EncTrajectory {
    private List<EncTimeLocationData> eTlds;

    private String path;

    public EncTrajectory(Trajectory trajectory) {
        this.path = trajectory.getPath();
        List<TimeLocationData> tlds = trajectory.getTlds();
        this.eTlds = new ArrayList<>();
        for (TimeLocationData tld : tlds) {
            eTlds.add(tld.encrypt());
        }
    }
    public List<EncTimeLocationData> geteTlds() {
        return eTlds;
    }

    public void seteTlds(List<EncTimeLocationData> eTlds) {
        this.eTlds = eTlds;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "EncTrajectory{" +
                "eTlds=" + eTlds +
                ", path='" + path + '\'' +
                '}';
    }
}
