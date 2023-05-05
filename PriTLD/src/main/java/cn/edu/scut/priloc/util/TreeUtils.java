package cn.edu.scut.priloc.util;

import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.Trajectory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;

@Component
public class TreeUtils {


    public static String DBPath;

    public static String dataPath;

    static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    @Value("${tree.db-path}")
    public void setDBPath(String DBPath) {
        TreeUtils.DBPath = DBPath;
    }
    @Value("${tree.data-path}")
    public void setDataPath(String dataPath) {
        TreeUtils.dataPath = dataPath;
    }

    public static BTreePlus<BeginEndPath> getTree(){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DBPath +"\\tree.txt"));
            return (BTreePlus<BeginEndPath>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void storeTree(BTreePlus<BeginEndPath> bTreePlus){
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(DBPath +"\\tree.txt"));
            outputStream.writeObject(bTreePlus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setETlds(EncTrajectory encTrajectory,String name){
        try {
            //创建以该用户id命名的目录
            File file = new File(DBPath +"\\"+encTrajectory.getUserId());
            file.mkdir();
            name = name.split("\\.")[0]+".txt";
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file.getPath() + "\\" + name));
            outputStream.writeObject(encTrajectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static EncTrajectory getETlds(BeginEndPath bep){
        try {
            String name = bep.getPath().split("\\.")[0];
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DBPath+"\\"+bep.getUserId()+"\\"+name+".txt"));
            return (EncTrajectory) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Trajectory getTlds(BeginEndPath bep) throws IOException, ClassNotFoundException {
        String name=bep.getPath();
        //读出数据集的数据
        TrajectoryReader reader = new TrajectoryReader(dataPath+"\\"+bep.getUserId()+"\\Trajectory\\"+name);
        return reader.loadWithBep(bep);
    }
}
