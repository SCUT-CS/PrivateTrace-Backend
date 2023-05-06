//package cn.edu.scut.priloc;
//
//import cn.edu.scut.priloc.mapper.BTreePlus;
//import cn.edu.scut.priloc.mapper.BepReader;
//import cn.edu.scut.priloc.mapper.Entry;
//import cn.edu.scut.priloc.pojo.*;
//import cn.edu.scut.priloc.service.EncTrajectoryService;
//import cn.edu.scut.priloc.service.impl.EncTrajectoryServiceImpl;
//import cn.edu.scut.priloc.util.SpringUtils;
//import cn.edu.scut.priloc.util.TrajectoryReader;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.stereotype.Controller;
//
//import javax.imageio.plugins.tiff.BaselineTIFFTagSet;
//import java.awt.*;
//import java.io.*;
//import java.text.ParseException;
//import java.util.Date;
//import java.util.List;
//
//@SpringBootTest
//public class createUnencDataBase {
//    @Test
//    public void testCreate() throws FileNotFoundException, ParseException, IOException{
//        File file = new File("C:\\Users\\18124\\Desktop\\Data");
//        File[] files = file.listFiles();
//        for(int i = 0; i < files.length; i++){
//            String id = files[i].getName();
//            File userFile = new File("C:\\Users\\18124\\Desktop\\UnencDataBase\\" + id);
//            if(!userFile.exists()){
//                userFile.mkdir();
//            }
//            File trajectory = files[i].listFiles()[0];
//            File[] trajectories = trajectory.listFiles();
//            int l = trajectories.length;
//            for (int j = 0; j < l; j++) {
//                TrajectoryReader reader = new TrajectoryReader(trajectories[j].getPath());
//                Trajectory trajectoryOfThisFile = reader.load(id);
//                String fileName = trajectories[j].getName();
//                fileName = fileName.substring(0, fileName.length() - 4);
//                String dataBasePath = "C:\\Users\\18124\\Desktop\\DataBase\\" + id + "\\" + fileName + ".txt";
//                File dataBaseFile = new File(dataBasePath);
//                ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(dataBaseFile));
//                EncTrajectory encTrajectory = null;
//                try {
//                    encTrajectory = (EncTrajectory) inputStream.readObject();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//                List<EncTimeLocationData> encTimeLocationDataList = encTrajectory.geteTlds();
//                List<TimeLocationData> timeLocationDataList = trajectoryOfThisFile.getTlds();
//                for (int k = 0; k < encTimeLocationDataList.size(); k++) {
//                    Date encDate = encTimeLocationDataList.get(k).getDate();
//                    timeLocationDataList.get(k).setDate(encDate);
//                }
//                String path = "C:\\Users\\18124\\Desktop\\UnencDataBase\\" + id + "\\" + fileName + ".txt";
//                File newFile = new File(path);
//                if(!newFile.exists()){
//                    newFile.createNewFile();
//                }
//                ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(newFile));
//                outputStream.writeObject(trajectoryOfThisFile);
//            }
//        }
//    }
//}
