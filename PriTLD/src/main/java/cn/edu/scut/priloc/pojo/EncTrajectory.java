package cn.edu.scut.priloc.pojo;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EncTrajectory implements Serializable {
    private List<EncTimeLocationData> eTlds;

    private String userId;
    private String path;

    public EncTrajectory(Trajectory trajectory) {
        this.userId=trajectory.getUserId();
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "EncTrajectory{" +
                "eTlds=" + eTlds +
                ", userId='" + userId + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
