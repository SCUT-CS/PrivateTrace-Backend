package cn.edu.scut.priloc.pojo;

import java.util.Date;

public class tableData {
    private Date beginTime;
    private Date endTime;
    private String userId;

    public tableData(BeginEndPath bep) {
        this.beginTime = new Date(bep.getBeginTime());
        this.endTime = new Date(bep.getEndTime());
        this.userId = bep.getUserId();
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "tableData{" +
                "beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", userId='" + userId + '\'' +
                '}';
    }
}
