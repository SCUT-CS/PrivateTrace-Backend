package cn.edu.scut.priloc.service;

import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.Trajectory;
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
        trajectory.setPath(path);
        //将轨迹存储到数据库（序列化）
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
        outputStream.writeObject(trajectory);
    }

    @Test
    public void testEncrypt() throws FileNotFoundException, ParseException {
        TrajectoryReader reader=new TrajectoryReader("E:\\GitHub\\PriTLD\\Data\\000\\Trajectory\\20081024020959.plt");
        Trajectory trajectory=reader.load("001");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("test");
        EncTrajectory encTrajectory = new EncTrajectory(trajectory);
        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());
    }
}
