package cn.edu.scut.priloc.service;

import Priloc.data.EncTrajectory;
import cn.edu.scut.priloc.mapper.BPlusTree;

import java.util.List;

public interface EncTrajectoryService {
    List<EncTrajectory> selectByPage(int currentPage, int pageSize);

    void add(BPlusTree bPlusTree, EncTrajectory encTrajectory);

    List<EncTrajectory> selectByETLDs(BPlusTree bPlusTree,EncTrajectory encTrajectory);
}
