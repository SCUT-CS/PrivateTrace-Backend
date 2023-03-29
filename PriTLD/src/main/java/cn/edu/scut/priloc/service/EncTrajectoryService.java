package cn.edu.scut.priloc.service;

import cn.edu.scut.priloc.pojo.*;

import java.util.List;

public interface EncTrajectoryService {
    List<EncTrajectory> selectByPage(int currentPage, int pageSize);

    void add(EncTrajectory encTrajectory);

    List<EncTrajectory> selectByETLDs(EncTrajectory encTrajectory);
}
