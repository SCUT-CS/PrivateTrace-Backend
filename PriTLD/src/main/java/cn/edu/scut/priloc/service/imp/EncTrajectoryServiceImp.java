package cn.edu.scut.priloc.service.imp;

import Priloc.data.EncTrajectory;
import cn.edu.scut.priloc.mapper.BPlusTree;
import cn.edu.scut.priloc.service.EncTrajectoryService;

import java.util.List;
/*
* 索引树功能的具体实现
* 索引树代码写在这里（大概
*/
public class EncTrajectoryServiceImp implements EncTrajectoryService {
    public List<EncTrajectory> selectByPage(int currentPage, int pageSize) {
        return null;
    }

    public void add(BPlusTree bPlusTree,EncTrajectory encTrajectory){

    }
    public List<EncTrajectory> selectByETLDs(BPlusTree bPlusTree,EncTrajectory encTrajectory) {
        return null;
    }
}
