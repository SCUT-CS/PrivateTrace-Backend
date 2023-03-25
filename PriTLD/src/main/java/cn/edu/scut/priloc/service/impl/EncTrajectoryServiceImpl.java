package cn.edu.scut.priloc.service.impl;

import Priloc.data.EncTrajectory;
import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.mapper.Entry;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.service.EncTrajectoryService;

import java.io.*;
import java.util.List;
/*
* 索引树功能的具体实现
* 索引树代码写在这里（大概
*/
public class EncTrajectoryServiceImpl implements EncTrajectoryService {
    private  static BTreePlus bTreePlus;

    private void getTree(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(""));
            bTreePlus = (BTreePlus) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void storeTree(){
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(""));
            outputStream.writeObject(bTreePlus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<EncTrajectory> selectByPage(int currentPage, int pageSize) {
        return null;
    }

    @Override
    public void add(EncTrajectory encTrajectory) {
        //添加到索引树上 调用service方法
        BeginEndPath beginEndPath=new BeginEndPath(encTrajectory);
        Entry<BeginEndPath> entry=new Entry<>(beginEndPath.getBeginTime(), beginEndPath);
        bTreePlus.addEntry(entry);
        //序列化新的树
    }

    @Override
    public List<EncTrajectory> selectByETLDs(EncTrajectory encTrajectory) {
        return null;
    }
}
