package cn.edu.scut.priloc.service.impl;

import cn.edu.scut.priloc.pojo.*;
import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.mapper.Entry;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.service.EncTrajectoryService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/*
* 索引树功能的具体实现
* 索引树代码写在这里（大概
*/
public class EncTrajectoryServiceImpl implements EncTrajectoryService {

    private BTreePlus getTree(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("PriTLD/DataBase"));
            BTreePlus bTreePlus = (BTreePlus) inputStream.readObject();
            return bTreePlus;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void storeTree(BTreePlus bTreePlus){
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream("PriTLD/DataBase"));
            outputStream.writeObject(bTreePlus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private EncTrajectory getETlds(String path){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
            EncTrajectory encTrajectory = (EncTrajectory) inputStream.readObject();
            return encTrajectory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<EncTrajectory> selectByPage(int currentPage, int pageSize) {
        return null;
    }

    @Override
    public void add(EncTrajectory encTrajectory) {
        String path="PriTLD/DataBase/";
        //创建以该用户id命名的目录,文件名为当前时间
        File file = new File(path+"001");
        file.mkdir();
        encTrajectory.setPath(path+"/"+System.currentTimeMillis());
        //将轨迹存储到数据库（反序列化）
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(encTrajectory.getPath()));
            outputStream.writeObject(encTrajectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //添加到索引树上 调用service方法
        BeginEndPath beginEndPath=new BeginEndPath(encTrajectory);
        Entry<BeginEndPath> entry=new Entry<>(beginEndPath.getBeginTime(), beginEndPath);
        BTreePlus bTreePlus=getTree();
        bTreePlus.addEntry(entry);
        //序列化新的树
        storeTree(bTreePlus);
    }

    @Override
    public List<EncTrajectory> selectByETLDs(EncTrajectory encTrajectory) {
        BeginEndPath beginEndPath=new BeginEndPath(encTrajectory);
        BTreePlus bTreePlus=getTree();
        ArrayList<BeginEndPath> list = bTreePlus.find(beginEndPath);
        List<EncTrajectory> eTldsList=new ArrayList<>();
        for (BeginEndPath o : list) {
            //读入磁盘位置中的加密轨迹
            eTldsList.add(getETlds(o.getPath()));
        }
        return eTldsList;
    }
}
