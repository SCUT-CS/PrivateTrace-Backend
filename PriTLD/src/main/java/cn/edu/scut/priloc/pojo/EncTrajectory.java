package cn.edu.scut.priloc.pojo;


import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class EncTrajectory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private List<EncTimeLocationData> eTlds;

    private String userId;


    public EncTrajectory(){}

    public EncTrajectory(List<EncTimeLocationData> eTlds, String userId) {
        this.eTlds = eTlds;
        this.userId = userId;
    }

    public List<EncTimeLocationData> geteTlds() {
        return eTlds;
    }

    public void seteTlds(List<EncTimeLocationData> eTlds) {
        this.eTlds = eTlds;
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
                '}';
    }
}
