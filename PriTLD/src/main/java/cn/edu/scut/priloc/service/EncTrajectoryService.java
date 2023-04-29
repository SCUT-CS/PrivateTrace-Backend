package cn.edu.scut.priloc.service;

import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.Trajectory;

import java.io.IOException;
import java.util.List;

public interface EncTrajectoryService {
    List<EncTrajectory> selectByPage(int currentPage, int pageSize);

    void add(EncTrajectory encTrajectory);

    List<EncTrajectory> selectByETLDs(EncTrajectory encTrajectory);

    EncTrajectory encrypt(Trajectory trajectory) throws IOException;

    Trajectory decrypt(EncTrajectory encTrajectory);

    Boolean query(List<EncTrajectory> encTrajectories,EncTrajectory encTrajectory);

}
