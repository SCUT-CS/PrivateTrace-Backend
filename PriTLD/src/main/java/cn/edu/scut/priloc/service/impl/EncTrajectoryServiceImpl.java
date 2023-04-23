package cn.edu.scut.priloc.service.impl;

import Priloc.area.basic.EncryptedCircle;
import Priloc.data.EncTmLocData;
import Priloc.protocol.CCircleTree;
import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.mapper.Entry;
import cn.edu.scut.priloc.pojo.*;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.util.SpringUtils;
import cn.edu.scut.priloc.util.TreeUtils;
import org.apache.commons.math3.util.FastMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/*
* 索引树功能的具体实现
* 索引树代码写在这里（大概
*/
@Service
public class EncTrajectoryServiceImpl implements EncTrajectoryService {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        BTreePlus bTreePlus=TreeUtils.getTree();
        bTreePlus.addEntry(entry);
        //序列化新的树
        TreeUtils.storeTree(bTreePlus);
    }

    @Override
    public List<EncTrajectory> selectByETLDs(EncTrajectory encTrajectory) {
        BeginEndPath beginEndPath=new BeginEndPath(encTrajectory);
        BTreePlus bTreePlus= TreeUtils.getTree();
        ArrayList<BeginEndPath> list = bTreePlus.find(beginEndPath);
        List<EncTrajectory> eTldsList=new ArrayList<>();
        for (BeginEndPath o : list) {
            //读入磁盘位置中的加密轨迹
            eTldsList.add(TreeUtils.getETlds(o.getPath()));
        }
        return eTldsList;
    }

    @Override
    public EncTrajectory encrypt(Trajectory trajectory){
        EncTrajectoryService serviceProxy = SpringUtils.getBean(EncTrajectoryService.class);
        List<TimeLocationData> tlds = trajectory.getTlds();
        List<Future<ArrayList<EncTimeLocationData>>> futures = new ArrayList<>();
        int size=tlds.size();
        int step=size/5+1;
        for(int i=0,j=0; i< size; i+=step,j++){
            futures.add(serviceProxy.doEncrypt(tlds, i, FastMath.min(i + step, size)));
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
        EncTrajectory encTrajectory = new EncTrajectory(trajectory);
        encTrajectory.seteTlds(eTlds);
        return encTrajectory;
    }

    @Async("taskExecutor")
    public Future<ArrayList<EncTimeLocationData>> doEncrypt(List<TimeLocationData> tlds, int begin, int end) {
        logger.info("异步线程启动，between"+begin+" and "+end);
        ArrayList<EncTimeLocationData> eTlds=new ArrayList<>();
        for (int i = begin; i < end; i++) {
            eTlds.add(tlds.get(i).encrypt());
        }
        return new AsyncResult<>(eTlds);
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
