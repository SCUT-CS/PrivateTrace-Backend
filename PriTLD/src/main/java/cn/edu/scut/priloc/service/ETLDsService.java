package cn.edu.scut.priloc.service;

import Priloc.data.EncTrajectory;

import java.util.List;

public interface ETLDsService {
    List<EncTrajectory> selectByPage(int currentPage, int pageSize);

    void add(EncTrajectory encTrajectory);

    List<EncTrajectory> selectByETLDs(EncTrajectory encTrajectory);
}
