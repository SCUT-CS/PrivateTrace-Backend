package cn.edu.scut.priloc.service;

import Priloc.data.EncTrajectory;
import Priloc.data.Trajectory;
import cn.edu.scut.priloc.mapper.BPlusTree;

import java.util.List;

public interface TrajectoryService {
    void add(BPlusTree bPlusTree, Trajectory encTrajectory);

    List<Trajectory> selectByTLDs(BPlusTree bPlusTree, Trajectory Trajectory);
}
