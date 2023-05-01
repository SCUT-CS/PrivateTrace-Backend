import cn.edu.scut.priloc.pojo.EncTimeLocationData;
import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.TimeLocationData;
import cn.edu.scut.priloc.pojo.Trajectory;
import cn.edu.scut.priloc.util.TrajectoryReader;

import java.io.FileNotFoundException;
import java.text.ParseException;

public class Test1 {
    public static void main(String[] args) throws FileNotFoundException, ParseException {
        //获取明文轨迹
        TrajectoryReader reader=new TrajectoryReader("E:\\GitHub\\PriTLD\\Data\\000\\Trajectory\\20081024020959.plt");
        Trajectory trajectory=reader.load("001");
        for (TimeLocationData tld : trajectory.getTlds()) {
            System.out.println(tld);
        }
//        EncTrajectory encTrajectory = new EncTrajectory(trajectory);
//        for(EncTimeLocationData eTld : encTrajectory.geteTlds()){
//            System.out.println(eTld.getEncPoint());
//        }
        //System.out.println(JSON.toJSONString(trajectory));
    }
}
