import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.pojo.EncTrajectory;

import javax.imageio.plugins.tiff.BaselineTIFFTagSet;
import java.io.*;

public class Test3 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String path ="C:\\Users\\18124\\Desktop\\DataBase\\tree.txt";
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        BTreePlus<BeginEndPath> bTreePlus = (BTreePlus<BeginEndPath>) ois.readObject();
        ois.close();
        FileWriter fileWriter = new FileWriter("C:\\Users\\18124\\Desktop\\DataBase\\tree1.txt");
        fileWriter.write(bTreePlus.getRoot().getKeysSize());
        System.out.println(bTreePlus.getRoot().getKeysSize());
        }
}
