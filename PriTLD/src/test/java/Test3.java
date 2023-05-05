import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.Trajectory;

import javax.imageio.plugins.tiff.BaselineTIFFTagSet;
import java.io.*;

public class Test3 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("C:\\Users\\18124\\Desktop\\DataBase\\000\\20081024020959.txt"));
        EncTrajectory encTrajectory = (EncTrajectory) objectInputStream.readObject();
        System.out.println("this is the encTrajectory");
        System.out.println(encTrajectory.geteTlds());
        System.out.println("this is the Trajectory");
        ObjectInputStream objectInputStream1 = new ObjectInputStream(new FileInputStream("C:\\Users\\18124\\Desktop\\UnencDataBase\\000\\20081024020959.txt"));
        Trajectory trajectory = (Trajectory) objectInputStream1.readObject();
        System.out.println(trajectory.getTlds());
        System.out.println("flag!");
        }
}
