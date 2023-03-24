package cn.edu.scut.priloc.service;

import Priloc.data.EncTrajectory;
import cn.edu.scut.priloc.mapper.BTreePlus;

import java.util.List;

public interface EncTrajectoryService {
    List<EncTrajectory> selectByPage(int currentPage, int pageSize);

    void add(BTreePlus bTreePlus, EncTrajectory encTrajectory);

    List<EncTrajectory> selectByETLDs(BTreePlus bTreePlus,EncTrajectory encTrajectory);
}
