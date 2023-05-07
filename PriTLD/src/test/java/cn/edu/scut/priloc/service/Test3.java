package cn.edu.scut.priloc.service;

import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.mapper.Entry;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.Trajectory;
import cn.edu.scut.priloc.util.TrajectoryReader;
import cn.edu.scut.priloc.util.TreeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class Test3 {
    @Autowired
    EncTrajectoryService encTrajectoryService;
    @Test
    public  void creatTree() throws IOException, ClassNotFoundException {
        BTreePlus<BeginEndPath> bTreePlus = new BTreePlus<>();
        for (int i = 0; i < 3; i++) {
            File rootFile = new File("E:\\GitHub\\PriTLD\\PriTLD\\DataBase\\00" + i);
            File[] files = rootFile.listFiles();
            assert files != null;

            for (File file : files) {
                String name= file.getName();
                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                EncTrajectory encTrajectory = (EncTrajectory) inputStream.readObject();
                BeginEndPath beginEndPath = new BeginEndPath(encTrajectory);
                beginEndPath.setPath(name);
                beginEndPath.setUserId("00"+i);
                Entry<BeginEndPath> entryNode = new Entry<>(beginEndPath.getBeginTime(), beginEndPath);
                bTreePlus.addEntry(entryNode);
            }


        }
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(new File("E:\\GitHub\\PriTLD\\PriTLD\\DataBase\\tree.txt")));
        outputStream.writeObject(bTreePlus);
        outputStream.close();
    }

    @Test
    public void addData() throws IOException, ClassNotFoundException, ParseException {
        BTreePlus<BeginEndPath> bTreePlus = TreeUtils.getTree();
        for (int i = 4; i <=4; i++) {
            File rootFile = new File("E:\\GitHub\\PriTLD\\Data\\00" + i+"\\Trajectory");
            File[] files = rootFile.listFiles();
            assert files != null;
            for (File file : files) {
                String name= file.getName();
                TrajectoryReader reader = new TrajectoryReader(file);
                List<Trajectory> trajectoryList = reader.load("00" + i);
                for (Trajectory trajectory : trajectoryList) {
                    if (trajectory.getTlds().size() == 1) continue;
                    EncTrajectory encTrajectory = encTrajectoryService.encrypt(trajectory);
                    TreeUtils.setETldsAndTlds(encTrajectory, trajectory);
                    BeginEndPath beginEndPath = new BeginEndPath(encTrajectory);
                    beginEndPath.setPath(name);
                    beginEndPath.setUserId("00" + i);
                    Entry<BeginEndPath> entryNode = new Entry<>(beginEndPath.getBeginTime(), beginEndPath);
                    bTreePlus.addEntry(entryNode);
                }
            }
        }
        TreeUtils.storeTree(bTreePlus);
    }

    @Test
    public void find() throws IOException, ClassNotFoundException {
        File[] files = new File("E:\\GitHub\\PriTLD\\PriTLD\\DataBase\\004").listFiles();
        assert files != null;
        for (File file : files) {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
            EncTrajectory encTrajectory = (EncTrajectory) inputStream.readObject();
            if(encTrajectory.geteTlds().size()>100) continue;
            ArrayList<BeginEndPath> beginEndPathArrayList = encTrajectoryService.selectByETLDs(encTrajectory);
            if(beginEndPathArrayList.isEmpty()) continue;
            List<EncTrajectory> encTrajectories=new ArrayList<>();
            for (BeginEndPath beginEndPath : beginEndPathArrayList) {
                encTrajectories.add(TreeUtils.getETlds(beginEndPath));
            }
            boolean query=false;
            try {
                query = encTrajectoryService.query(encTrajectories, encTrajectory);
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println(file.getName()+"===>>"+query);
            if (query) break;
        }

    }
}
