import cn.edu.scut.priloc.pojo.Trajectory;
import cn.edu.scut.priloc.util.TrajectoryReader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.ParseException;

public class Test2 {
    public static void main(String[] args) throws IOException, ParseException {
        //获取明文轨迹
        TrajectoryReader reader=new TrajectoryReader("E:\\GitHub\\PriTLD\\Data\\000\\Trajectory\\20081024020959.plt");
        Trajectory trajectory=reader.load("001");

        File file = new File("PriTLD/DataBase/001");
        System.out.println(file.getAbsoluteFile());
        System.out.println(file.mkdir());

        //用用户id和时间作为文件名
        String path="PriTLD/DataBase/"+trajectory.getUserId()+"/"+System.currentTimeMillis();
        trajectory.setPath(path);
        //将轨迹存储到数据库（反序列化）
        ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path));
        outputStream.writeObject(trajectory);
        //System.out.println(JSON.toJSONString(trajectory));
    }
}
