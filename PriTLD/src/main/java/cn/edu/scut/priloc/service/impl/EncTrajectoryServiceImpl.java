package cn.edu.scut.priloc.service.impl;

import Priloc.data.EncTrajectory;
import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.service.EncTrajectoryService;

import java.util.List;
/*
* 索引树功能的具体实现
* 索引树代码写在这里（大概
*/
public class EncTrajectoryServiceImpl implements EncTrajectoryService {
    public List<EncTrajectory> selectByPage(int currentPage, int pageSize) {
        return null;
    }

    public void add(BTreePlus bTreePlus, EncTrajectory encTrajectory){

    }
    public List<EncTrajectory> selectByETLDs(BTreePlus bTreePlus,EncTrajectory encTrajectory) {
        return null;
    }
}
