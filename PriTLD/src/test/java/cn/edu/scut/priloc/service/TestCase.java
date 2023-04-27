package cn.edu.scut.priloc.service;

import cn.edu.scut.priloc.pojo.*;
import cn.edu.scut.priloc.util.TrajectoryReader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.io.*;
import java.text.ParseException;

@SpringBootTest
public class TestCase {
    @Autowired
    EncTrajectoryService etldsService;
    @Test
    public void testAdd() throws IOException, ParseException {
        TrajectoryReader reader=new TrajectoryReader("E:\\GitHub\\PriTLD\\Data\\000\\Trajectory\\20081024020959.plt");
        Trajectory trajectory=reader.load("001");
        File file = new File("DataBase/001");
        System.out.println(file.getAbsoluteFile());
        System.out.println(file.mkdir());

        //用用户id和时间作为文件名
        String path="DataBase/"+trajectory.getUserId()+"/"+System.currentTimeMillis()+".txt";
        //将轨迹存储到数据库（序列化）
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
        outputStream.writeObject(trajectory);
    }

    @Test
    public void testSetDate() throws FileNotFoundException, ParseException {
        File file = new File("C:\\Users\\18124\\Desktop\\Data\\000\\Trajectory");
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            System.out.println("第"+i+"个文件");
            TrajectoryReader reader=new TrajectoryReader(files[i]);
            Trajectory trajectory=reader.load("001");
            System.out.println(trajectory.getTlds().get(0).getDate());
        }

    }

    @Test
    public void testEncrypt() throws IOException, ParseException {
        TrajectoryReader reader=new TrajectoryReader("E:\\GitHub\\PriTLD\\Data\\000\\Trajectory\\20081024020959.plt");
        Trajectory trajectory=reader.load("001");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("test");
        EncTrajectory encTrajectory = etldsService.encrypt(trajectory);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
        for (EncTimeLocationData etld : encTrajectory.geteTlds()) {
            System.out.println(etld.getDate());
        }
    }

    @Test
    public void testTree() throws IOException, ParseException, ClassNotFoundException {
        //TODO
        //测试树的建立，路径需要修改
        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("C:\\Users\\18124\\Desktop\\DataBase\\001\\20081205002359.txt"));
        EncTrajectory eTdls = (EncTrajectory) inputStream.readObject();
        eTdls.setUserId("000");
        etldsService.selectByETLDs(eTdls);
        System.out.println(eTdls.geteTlds());
    }
}
