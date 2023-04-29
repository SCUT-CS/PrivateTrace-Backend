package cn.edu.scut.priloc.util;

import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.pojo.EncTrajectory;

import java.io.*;
import java.text.SimpleDateFormat;

public class TreeUtils {

    static String path=System.getProperty("user.dir")+"\\PriTLD\\DataBase";
    public static BTreePlus<BeginEndPath> getTree(){
        try {
            System.out.println(path);
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path+"\\tree.txt"));
            BTreePlus<BeginEndPath> bTreePlus = (BTreePlus<BeginEndPath>) inputStream.readObject();
            return bTreePlus;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void storeTree(BTreePlus bTreePlus){
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream(path+"\\tree.txt"));
            outputStream.writeObject(bTreePlus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static EncTrajectory setETlds(EncTrajectory encTrajectory){
        try {
            //创建以该用户id命名的目录,文件名为当前时间
            File file = new File(path+"\\001");
            file.mkdir();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String name=format.format(encTrajectory.geteTlds().get(0).getDate())+".txt";
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path+"\\"+encTrajectory.getUserId()+"\\"+name));
            return (EncTrajectory) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
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
}
