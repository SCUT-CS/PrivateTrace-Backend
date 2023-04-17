package cn.edu.scut.priloc.service.impl;

import Priloc.area.basic.EncryptedCircle;
import Priloc.data.EncTmLocData;
import Priloc.protocol.CCircleTree;
import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.mapper.Entry;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.pojo.EncTimeLocationData;
import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.Trajectory;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
/*
* 索引树功能的具体实现
* 索引树代码写在这里（大概
*/
@Service
public class EncTrajectoryServiceImpl implements EncTrajectoryService {

    private BTreePlus getTree(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("E:\\GitHub\\PriTLD\\PriTLD\\DataBase"));
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
            outputStream = new ObjectOutputStream(new FileOutputStream("E:\\GitHub\\PriTLD\\PriTLD\\DataBase"));
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

    @Override
    public EncTrajectory encrypt(Trajectory trajectory) {
        EncTrajectory encTrajectory=new EncTrajectory(trajectory);
        return encTrajectory;
    }

    @Override
    public Boolean query(List<EncTrajectory> encTrajectories,EncTrajectory encTrajectory) {
        List<Priloc.data.EncTrajectory> encCircles = new ArrayList<>();
        for (EncTrajectory encTrajectory1 : encTrajectories) {
            Priloc.data.EncTrajectory temp = getEncTrajectory(encTrajectory1);
            encCircles.add(temp);
        }
        // 在EncTrajectory.java中添加了一个构造函数 然后install
//    public EncTrajectory(List<EncTmLocData> eTLDs) {
//            this.eTLDs = eTLDs;
//        }
        CCircleTree cCircleTree = new CCircleTree();
        for (int i = 0; i < encCircles.size(); i++) {
            cCircleTree.add(encCircles.get(i));
        }
        try {
            cCircleTree.init(true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        boolean result = cCircleTree.compare(getEncTrajectory(encTrajectory));
        return result;
    }
    private static Priloc.data.EncTrajectory getEncTrajectory(EncTrajectory encTrajectory1) {
        List<EncTimeLocationData> eTlds2 = encTrajectory1.geteTlds();
        List<EncTmLocData> eTLDs = new ArrayList();
        EncTmLocData pETLD = null;
        for (int i = 0; i < eTlds2.size(); ++i) {
            EncTmLocData cETLD = new EncTmLocData(new EncryptedCircle(eTlds2.get(i).getEncPoint(), 200), eTlds2.get(i).getDate());
            if (pETLD != null) {
                pETLD.setNext(cETLD);
            }
            cETLD.setPrevious(pETLD);
            eTLDs.add(cETLD);
            pETLD = cETLD;
        }
        Priloc.data.EncTrajectory temp = new Priloc.data.EncTrajectory(eTLDs);
        return temp;
    }
}
