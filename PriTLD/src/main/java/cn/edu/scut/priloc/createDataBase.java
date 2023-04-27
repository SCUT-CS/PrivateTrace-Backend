package cn.edu.scut.priloc;

import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.mapper.BepReader;
import cn.edu.scut.priloc.mapper.Entry;
import cn.edu.scut.priloc.pojo.*;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.service.impl.EncTrajectoryServiceImpl;
import cn.edu.scut.priloc.util.SpringUtils;
import cn.edu.scut.priloc.util.TrajectoryReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Controller;

import javax.imageio.plugins.tiff.BaselineTIFFTagSet;
import java.awt.*;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
@SpringBootTest
public class createDataBase {

    @Test
    public void testCreate() throws FileNotFoundException, ParseException, IOException{
        File file = new File("C:\\Users\\18124\\Desktop\\Data");
        File[] files = file.listFiles();
        BTreePlus<BeginEndPath> bTreePlus = new BTreePlus<>();
        Entry<BeginEndPath> entryNode;
        for(int i = 0; i < files.length; i++){
            String id = files[i].getName();
            File userFile = new File("C:\\Users\\18124\\Desktop\\DataBase\\" + id);
            if(!userFile.exists()){
                userFile.mkdir();
            }
            File trajectory = files[i].listFiles()[0];
            File[] trajectories = trajectory.listFiles();
            int l = trajectories.length;
            for (int j = 0; j < l; j++) {
                TrajectoryReader reader = new TrajectoryReader(trajectories[j].getPath());
                Trajectory trajectoryOfThisFile = reader.load(id);
                EncTrajectoryService encTrajectoryService = SpringUtils.getBean(EncTrajectoryService.class);
                EncTrajectory encTrajectory = encTrajectoryService.encrypt(trajectoryOfThisFile);
                String fileName = trajectories[j].getName();
                fileName = fileName.substring(0, fileName.length() - 4);
                String path = "C:\\Users\\18124\\Desktop\\DataBase\\" + id + "\\" + fileName + ".txt";
                File newFile = new File(path);
                if(!newFile.exists()){
                    newFile.createNewFile();
                }
                //encTrajectory.setPath(path);
                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(newFile));
                outputStream.writeObject(encTrajectory);
                BeginEndPath beginEndPath = new BeginEndPath(encTrajectory);
                String abstractPath = "DataBase\\" + id + "\\" + fileName + ".txt";
                beginEndPath.setPath(abstractPath);
                beginEndPath.setUserId(id);
                entryNode = new Entry<>(beginEndPath.getBeginTime(), beginEndPath);
                bTreePlus.addEntry(entryNode);
            }
        }
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("C:\\Users\\18124\\Desktop\\DataBase\\tree.txt"));
        objectOutputStream.writeObject(bTreePlus);
        objectOutputStream.close();
    }
}