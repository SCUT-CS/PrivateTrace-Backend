package cn.edu.scut.priloc.pojo;


import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EncTrajectory implements Serializable {

    @Serial
    private static final long serialVersionUID = 5509957259444506847L;
    private List<EncTimeLocationData> eTlds;

    private String userId;
    private String path;

    public EncTrajectory(){}
    public EncTrajectory(Trajectory trajectory) {
        this.userId=trajectory.getUserId();
        this.path = trajectory.getPath();
        this.eTlds=new ArrayList<>();
        /*List<TimeLocationData> tlds = trajectory.getTlds();
        this.eTlds = new ArrayList<>();
        StopWatch stopWatch = new StopWatch();
        int i=0;
        for (TimeLocationData tld : tlds) {
            stopWatch.start("第"+i+"次加密");
            eTlds.add(tld.encrypt());
            stopWatch.stop();
        }
        System.out.println(stopWatch.prettyPrint());*/
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
