package cn.edu.scut.priloc.service;

import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.Trajectory;
import cn.edu.scut.priloc.pojo.tableData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface EncTrajectoryService {
    List<EncTrajectory> selectByPage(int currentPage, int pageSize);

    List<tableData> showAll();

    EncTrajectory showByIndex(int index);

    void add(EncTrajectory encTrajectory,Trajectory trajectory);

    ArrayList<BeginEndPath> selectByETLDs(EncTrajectory encTrajectory);

    EncTrajectory encrypt(Trajectory trajectory) throws IOException;

    Trajectory decrypt(EncTrajectory encTrajectory);

    List<Trajectory> getTrajectoryList(ArrayList<BeginEndPath> beginEndPathArrayList,Trajectory trajectory) throws IOException, ClassNotFoundException;

    Boolean query(List<EncTrajectory> encTrajectories,EncTrajectory encTrajectory);

}
