package cn.edu.scut.priloc;

import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.mapper.Entry;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.Trajectory;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.util.TrajectoryReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@SpringBootTest
public class createTwoDataBase {

    @Autowired
    EncTrajectoryService  encTrajectoryService;
    @Test
    //结合createDataBase.java和createUnencDataBase.java，写一个同时能够处理两个数据库的程序
    public void testCreate() throws FileNotFoundException, ParseException, IOException {
        File file = new File("C:\\Users\\18124\\Desktop\\Data");
        File[] files = file.listFiles();
        BTreePlus<BeginEndPath> bTreePlus = new BTreePlus<>();
        Entry<BeginEndPath> entryNode;
        for (int i = 0; i < files.length; i++) {
            String id = files[i].getName();
            File userFile = new File("C:\\Users\\18124\\Desktop\\DataBase\\" + id);
            if (!userFile.exists()) {
                userFile.mkdir();
            }
            File trajectory = files[i].listFiles()[0];
            File[] trajectories = trajectory.listFiles();
            int l = trajectories.length;
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            for (int j = 0; j < l; j++) {
                TrajectoryReader reader = new TrajectoryReader(trajectories[j].getPath());
                List<Trajectory> trajectoryList = reader.load(id);
                for (Trajectory tlds : trajectoryList) {
                    EncTrajectory encTrajectory = encTrajectoryService.encrypt(tlds);
                    String name = format.format(tlds.getTlds().get(0).getDate());
                    String encPath = "C:\\Users\\18124\\Desktop\\DataBase\\" + id + "\\" + name + ".txt";
                    String unencPath = "C:\\Users\\18124\\Desktop\\UnencDataBase\\" + id + "\\" + name + ".txt";
                    File newFile = new File(encPath);
                    if (!newFile.exists()) {
                        newFile.createNewFile();
                    }
                    ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(newFile));
                    outputStream.writeObject(encTrajectory);
                    BeginEndPath beginEndPath = new BeginEndPath(encTrajectory);
                    String abstractPath = name + ".txt";
                    beginEndPath.setPath(abstractPath);
                    beginEndPath.setUserId(id);
                    entryNode = new Entry<>(beginEndPath.getBeginTime(), beginEndPath);
                    bTreePlus.addEntry(entryNode);
                    File newUnencFile = new File(unencPath);
                    if(!newFile.exists()){
                        newFile.createNewFile();
                    }
                    ObjectOutputStream outputStream2 = new ObjectOutputStream(new FileOutputStream(newUnencFile));
                    outputStream2.writeObject(tlds);
                }
            }
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("C:\\Users\\18124\\Desktop\\DataBase\\tree.txt"));
        objectOutputStream.writeObject(bTreePlus);
        objectOutputStream.close();
    }
}
