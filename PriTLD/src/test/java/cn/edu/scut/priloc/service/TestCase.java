package cn.edu.scut.priloc.service;

import cn.edu.scut.priloc.pojo.*;
import cn.edu.scut.priloc.util.TrajectoryReader;
import cn.edu.scut.priloc.util.TreeUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class TestCase {
    @Autowired
    EncTrajectoryService etldsService;

    @Test
    public void testAdd() throws IOException, ParseException {
        TrajectoryReader reader=new TrajectoryReader("E:\\GitHub\\PriTLD\\Data\\003\\Trajectory\\20081024020227.plt");
        List<Trajectory> trajectoryList = reader.load("003");
        Trajectory trajectory = trajectoryList.get(0);
        etldsService.add(etldsService.encrypt(trajectory),trajectory,"20081024020227.plt");
    }

    @Test
    public void testSetDate() throws FileNotFoundException, ParseException {
        File file = new File("C:\\Users\\18124\\Desktop\\Data\\001\\Trajectory\\20081029234123.plt");
        TrajectoryReader reader=new TrajectoryReader(file);
        List<Trajectory> trajectoryList = reader.load("000");
        System.out.println(trajectoryList.size());
        for (Trajectory trajectory : trajectoryList) {
            System.out.println(trajectory.getTlds().get(0).getDate());
        }

    }

    @Test
    public void testEncrypt() throws IOException, ParseException {
        TrajectoryReader reader=new TrajectoryReader("E:\\GitHub\\PriTLD\\Data\\000\\Trajectory\\20081024020959.plt");
        List<Trajectory> trajectoryList = reader.load("001");
        Trajectory trajectory = trajectoryList.get(0);
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("test");
        EncTrajectory encTrajectory = etldsService.encrypt(trajectory);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        for (EncTimeLocationData etld : encTrajectory.geteTlds()) {
            System.out.println(etld.getEncPoint());
        }
    }
    @Test
    public void testDecrypt() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("E:\\GitHub\\PriTLD\\PriTLD\\DataBase\\003\\20081024175854.txt"));
        EncTrajectory eTdls = (EncTrajectory) inputStream.readObject();
        eTdls.setUserId("003");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("test");
        Trajectory trajectory = etldsService.decrypt(eTdls);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        for (TimeLocationData tld : trajectory.getTlds()) {
            System.out.println(tld);
        }
    }

    @Test
    public void testTree() throws IOException, ParseException, ClassNotFoundException {
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("E:\\GitHub\\PriTLD\\PriTLD\\DataBase\\000\\20081024020959.txt"));
        EncTrajectory eTdls = (EncTrajectory) inputStream.readObject();
        eTdls.setUserId("000");
        ArrayList<BeginEndPath> beginEndPathList = etldsService.selectByETLDs(eTdls);
        System.out.println(beginEndPathList.size());
        List<EncTrajectory> encTrajectories = new ArrayList<>();
        for (BeginEndPath o : beginEndPathList) {
            //读入磁盘位置中的加密轨迹
            encTrajectories.add(TreeUtils.getETlds(o));
        }
        System.out.println(etldsService.query(encTrajectories,eTdls));
    }

    @Test
    public void testCut() throws IOException, ClassNotFoundException {
        ObjectInputStream inputStream1 = new ObjectInputStream(new FileInputStream("E:\\GitHub\\PriTLD\\PriTLD\\DataBase\\000\\20081211044624.txt"));
        EncTrajectory eTlds = (EncTrajectory) inputStream1.readObject();
        ObjectInputStream inputStream2 = new ObjectInputStream(new FileInputStream("E:\\GitHub\\PriTLD\\PriTLD\\UnencDataBase\\000\\20081211044624.txt"));
        Trajectory tlds = (Trajectory) inputStream2.readObject();
        eTlds.setUserId("000");
        ArrayList<BeginEndPath> beginEndPathList = etldsService.selectByETLDs(eTlds);
        System.out.println(beginEndPathList.size());
        List<Trajectory> trajectoryList = etldsService.getTrajectoryList(beginEndPathList, tlds);
        System.out.println(trajectoryList.size());
        for (Trajectory trajectory : trajectoryList) {
            System.out.println("---------");
            System.out.println(trajectory);
        }
    }

    @Test
    public void testShowByIndex(){
        for (int i = 0; i < 5; i++) {
            System.out.println("第"+i+"行==>"+etldsService.showByIndex(i).geteTlds().get(0).getDate());
        }

    }
}
