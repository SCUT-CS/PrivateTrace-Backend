package cn.edu.scut.priloc.mapper;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import lombok.Data;
import org.junit.Test;


@Data
public class BepReader {
    private String path;

    private File pltFile;

    private Scanner sc;

    public BepReader(){
    }

    public BepReader(String path){
        this.path = path;
    }

    public boolean hasNext(){
        return sc.hasNext();
    }

    private pojo.BeginEndPath getData(){
        Date beginTime = new Date();
        Date endTime = new Date();
        try {
            this.pltFile = new File(path);
            sc = new Scanner(pltFile);
            for (int i = 0; i < 6; i++) {
                sc.nextLine();
            }
            String[] tokens = sc.next().split(",");
            String beginTimeOfString = tokens[5] + " " + tokens[6];
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            beginTime = dateFormat.parse(beginTimeOfString);
            while(sc.hasNext()){
                tokens = sc.nextLine().split(",");
            }
            String endTimeOfString = tokens[5] + " " + tokens[6];
            endTime = dateFormat.parse(endTimeOfString);
        } catch (Exception e) {
            System.out.println("error");
            e.printStackTrace();
        }
        return new pojo.BeginEndPath(beginTime.getTime(), endTime.getTime(), this.path);
    }

    public BTreePlus readFromFiles(int numOfUsers){
        File file = new File(this.path);
        File[] files = file.listFiles();
        BTreePlus<pojo.BeginEndPath> btPlus=new BTreePlus<>();
        Entry<pojo.BeginEndPath> entryNode;
        for (int i = 0; i <numOfUsers ; i++) {
            String id = files[i].getName();
            File trajectory = files[i].listFiles()[0];
            File[] trajectories = trajectory.listFiles();
            int l = trajectories.length;
            for (int j = 0; j < l; j++) {
                BepReader bepReader = new BepReader(trajectories[j].getPath());
                pojo.BeginEndPath beginEndPath = bepReader.getData();
                beginEndPath.setUserId(id);
                entryNode =new Entry<>(beginEndPath.getBeginTime(),beginEndPath);
                btPlus.addEntry(entryNode);
            }
        }
        return btPlus;
    }

}
