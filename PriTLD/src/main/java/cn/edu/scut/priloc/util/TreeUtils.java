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
            System.out.println(DBPath);
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DBPath +"\\tree.txt"));
            return (BTreePlus<BeginEndPath>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void storeTree(BTreePlus bTreePlus){
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(DBPath +"\\tree.txt"));
            outputStream.writeObject(bTreePlus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void setETlds(EncTrajectory encTrajectory){
        try {
            //创建以该用户id命名的目录,文件名为当前时间
            File file = new File(DBPath +"\\001");
            file.mkdir();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String name=format.format(encTrajectory.geteTlds().get(0).getDate())+".txt";
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(DBPath + "\\" + encTrajectory.getUserId() + "\\" + name));
            outputStream.writeObject(encTrajectory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static EncTrajectory getETlds(String path){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
            return (EncTrajectory) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static Trajectory getTlds(BeginEndPath bep) throws IOException, ClassNotFoundException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String name=format.format(bep.getBeginTime())+".plt";
        //读出数据集的数据
        TrajectoryReader reader = new TrajectoryReader(dataPath+"\\"+bep.getUserId()+"\\Trajectory\\"+name);
        return reader.loadWithBep(bep);
    }
}
