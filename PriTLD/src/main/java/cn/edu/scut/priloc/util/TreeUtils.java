package cn.edu.scut.priloc.util;

import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.pojo.EncTrajectory;

import java.io.*;

public class TreeUtils {

    static String path=System.getProperty("user.dir")+"\\DataBase\\tree.txt";
    public static BTreePlus<BeginEndPath> getTree(){
        try {
            System.out.println(path);
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
            BTreePlus<BeginEndPath> bTreePlus = (BTreePlus<BeginEndPath>) inputStream.readObject();
            return bTreePlus;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void storeTree(BTreePlus bTreePlus){
        ObjectOutputStream outputStream = null;
        try {
            outputStream = new ObjectOutputStream(new FileOutputStream("E:\\GitHub\\PriTLD\\PriTLD\\DataBase"));
            outputStream.writeObject(bTreePlus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static EncTrajectory getETlds(String path){
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(path));
            EncTrajectory encTrajectory = (EncTrajectory) inputStream.readObject();
            return encTrajectory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
