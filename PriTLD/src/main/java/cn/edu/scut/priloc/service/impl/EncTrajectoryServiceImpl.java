package cn.edu.scut.priloc.service.impl;

import Priloc.area.basic.EncryptedCircle;
import Priloc.data.EncTmLocData;
import Priloc.protocol.CCircleTree;
import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.mapper.Entry;
import cn.edu.scut.priloc.pojo.*;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.util.CoordinateTransformUtil;
import cn.edu.scut.priloc.util.EncryptDecryptUtil;
import cn.edu.scut.priloc.util.TreeUtils;
import org.apache.commons.math3.util.FastMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class EncTrajectoryServiceImpl implements EncTrajectoryService {

    @Autowired
    EncryptDecryptUtil encryptDecryptUtil;

    @Override
    public List<EncTrajectory> selectByPage(int currentPage, int pageSize) {
        return null;
    }


    @Override
    public List<tableData> showAll() {
        BTreePlus<BeginEndPath> tree = TreeUtils.getTree();
        ArrayList<BeginEndPath> all = tree.findAll();
        List<tableData> tableDatas=new ArrayList<>();
        for (BeginEndPath beginEndPath : all) {
            tableDatas.add(new tableData(beginEndPath));
        }
        return tableDatas;
    }

    @Override
    public EncTrajectory showByIndex(int index) {
        BTreePlus<BeginEndPath> tree = TreeUtils.getTree();
        ArrayList<BeginEndPath> all = tree.findAll();
        return TreeUtils.getETlds(all.get(index));
    }

    @Override
    public void add(EncTrajectory encTrajectory,Trajectory trajectory,String name) {
        //将轨迹存储到数据库（序列化）
        TreeUtils.setETldsAndTlds(encTrajectory,trajectory,name);
        //添加到索引树上 调用service方法
        BeginEndPath beginEndPath=new BeginEndPath(encTrajectory);
        beginEndPath.setPath(name);
        Entry<BeginEndPath> entry=new Entry<>(beginEndPath.getBeginTime(), beginEndPath);
        BTreePlus<BeginEndPath> bTreePlus=TreeUtils.getTree();
        bTreePlus.addEntry(entry);
        //序列化新的树
        TreeUtils.storeTree(bTreePlus);
    }

    @Override
    public ArrayList<BeginEndPath> selectByETLDs(EncTrajectory encTrajectory) {
        BeginEndPath beginEndPath=new BeginEndPath(encTrajectory);
        BTreePlus<BeginEndPath> bTreePlus= TreeUtils.getTree();
        return bTreePlus.find(beginEndPath);
    }

    @Override
    public EncTrajectory encrypt(Trajectory trajectory){
        List<TimeLocationData> tlds = trajectory.getTlds();
        List<Future<ArrayList<EncTimeLocationData>>> futures = new ArrayList<>();
        int size=tlds.size();
        int step=size/5+1;
        for(int i=0,j=0; i< size; i+=step,j++){
            futures.add(encryptDecryptUtil.doEncrypt(tlds, i, Fastmath.min(i + step, size)));
        }
        List<EncTimeLocationData> eTlds = new ArrayList<>();
        for (Future<ArrayList<EncTimeLocationData>> future : futures) {
            try {
                eTlds.addAll(future.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return new EncTrajectory(eTlds,trajectory.getUserId());
    }

    @Override
    public List<Trajectory> getTrajectoryList(ArrayList<BeginEndPath> beginEndPathArrayList,Trajectory trajectory) throws IOException, ClassNotFoundException {
        List<Trajectory> trajectories = new ArrayList<>();
        List<TimeLocationData> temp= trajectory.getTlds();
        Instant beginTime = temp.get(0).getDate().toInstant();
        Instant endTime = temp.get(temp.size()-1).getDate().toInstant();
        for (BeginEndPath beginEndPath : beginEndPathArrayList) {
            Trajectory tlds = TreeUtils.getTlds(beginEndPath);
            List<TimeLocationData> tldList = tlds.getTlds();
            List<TimeLocationData> tempList=new ArrayList<>();
            //删除多余的tld
            boolean flag=false;
            for (int i = 0; i < tldList.size()-1 ; i++) {
                TimeLocationData tld1 = tldList.get(i);
                TimeLocationData tld2 = tldList.get(i+1);
                //最后一个小于beginTime
                if(tld1.getDate().toInstant().isBefore(beginTime)&&tld2.getDate().toInstant().isAfter(beginTime)){
                    flag=true;
                }
                //第一个大于beginTime
                if (tld1.getDate().toInstant().isAfter(beginTime)&&tld1.getDate().toInstant().isBefore(endTime)) flag=true;
                if(flag){
                    //坐标转换
                    double[] bd09 = CoordinateTransformUtil.wgs84tobd09(tld1.getLocation().getLongitude(), tld1.getLocation().getLatitude());
                    tld1.getLocation().setLongitude(bd09[0]);
                    tld1.getLocation().setLatitude(bd09[1]);
                    tempList.add(tld1);
                    //第一个大于endTime，仍可以加，后面不能加
                    if(tld1.getDate().toInstant().isAfter(endTime)){
                        flag=false;
                    }
                }
            }
//            System.out.println("---------");
//            for (TimeLocationData timeLocationData : tempList) {
//                System.out.println(timeLocationData.getDate());
//            }
            // TODO: 找出轨迹中的断点并分裂
            tlds.setTlds(tempList);
            trajectories.add(tlds);
        }
        return trajectories;
    }
    @Override
    public Trajectory decrypt(EncTrajectory encTrajectory) {
        List<EncTimeLocationData> eTlds = encTrajectory.geteTlds();
        List<Future<ArrayList<TimeLocationData>>> futures = new ArrayList<>();
        int size=eTlds.size();
        int step=size/5+1;
        for(int i=0,j=0; i< size; i+=step,j++){
            futures.add(encryptDecryptUtil.doDecrypt(eTlds, i, FastMath.min(i + step, size)));
        }
        List<TimeLocationData> tlds = new ArrayList<>();
        for (Future<ArrayList<TimeLocationData>> future : futures) {
            try {
                tlds.addAll(future.get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return new Trajectory(tlds,encTrajectory.getUserId());
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
        for (Priloc.data.EncTrajectory encCircle : encCircles) {
            cCircleTree.add(encCircle);
        }
        try {
            cCircleTree.init(true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        cCircleTree.addWork(getEncTrajectory(encTrajectory));
        return cCircleTree.run();
    }
    private static Priloc.data.EncTrajectory getEncTrajectory(EncTrajectory encTrajectory1) {
        List<EncTimeLocationData> eTlds2 = encTrajectory1.geteTlds();
        List<EncTmLocData> eTLDs = new ArrayList<>();
        EncTmLocData pETLD = null;
        for (EncTimeLocationData encTimeLocationData : eTlds2) {
            EncTmLocData cETLD = new EncTmLocData(new EncryptedCircle(encTimeLocationData.getEncPoint(), 200.0), encTimeLocationData.getDate());
            if (pETLD != null) {
                pETLD.setNext(cETLD);
            }
            cETLD.setPrevious(pETLD);
            eTLDs.add(cETLD);
            pETLD = cETLD;
        }
        return new Priloc.data.EncTrajectory(eTLDs);
    }
}
