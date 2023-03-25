package cn.edu.scut.priloc.pojo;

import Priloc.data.Trajectory;

import java.io.Serializable;
import java.util.Random;

public class BeginEndPath implements Serializable {
    private Long beginTime;

    private Long endTime;

    private String path;

    //endTime - beginTime
    private Long totalTime;

    private String userId;


    public BeginEndPath() {

    }

    public BeginEndPath(Trajectory trajectory) {
        this.beginTime = trajectory.getTLDs().get(0).getDate().getTime();
        this.endTime = trajectory.getTLDs().get(trajectory.getTLDs().size() - 1).getDate().getTime();
        this.totalTime = this.endTime - this.beginTime;
    }

    public BeginEndPath(Long beginTime, Long endTime, String path) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.path = path;
        this.totalTime = endTime - beginTime;
    }

    public BeginEndPath(Long beginTime, Long endTime, String path, Long totalTime, String userId) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.path = path;
        this.totalTime = totalTime;
        this.userId = userId;
    }

    /**
     * 获取
     * @return beginTime
     */
    public Long getBeginTime() {
        return beginTime;
    }

    /**
     * 设置
     * @param beginTime
     */
    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
        this.totalTime = this.endTime - this.beginTime;
    }

    /**
     * 获取
     * @return endTime
     */
    public Long getEndTime() {
        return endTime;
    }

    /**
     * 设置
     * @param endTime
     */
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
        this.totalTime = this.endTime - this.beginTime;
    }

    /**
     * 获取
     * @return path
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置
     * @param path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取
     * @return totalTime
     */
    public Long getTotalTime() {
        return totalTime;
    }

    public String toString() {
        return "BeginEndPath{beginTime = " + beginTime + ", endTime = " + endTime + ", path = " + path + ", totalTime = " + totalTime +", userId = "+ userId +"}";
    }

    /**
     * 设置
     * @param totalTime
     */
    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * 获取
     * @return userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
}
