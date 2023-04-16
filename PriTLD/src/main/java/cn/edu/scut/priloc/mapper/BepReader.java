package cn.edu.scut.priloc.mapper;

import cn.edu.scut.priloc.pojo.BeginEndPath;
import lombok.Data;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


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

    private BeginEndPath getData(){
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
        return new BeginEndPath(beginTime.getTime(), endTime.getTime(), this.path);
    }

    public BTreePlus readFromFiles(int numOfUsers){
        File file = new File(this.path);
        File[] files = file.listFiles();
        BTreePlus<BeginEndPath> btPlus=new BTreePlus<>();
        Entry<BeginEndPath> entryNode;
        for (int i = 0; i <numOfUsers ; i++) {
            String id = files[i].getName();
            File trajectory = files[i].listFiles()[0];
            File[] trajectories = trajectory.listFiles();
            int l = trajectories.length;
            for (int j = 0; j < l; j++) {
                BepReader bepReader = new BepReader(trajectories[j].getPath());
                BeginEndPath beginEndPath = bepReader.getData();
                beginEndPath.setUserId(id);
                entryNode =new Entry<>(beginEndPath.getBeginTime(),beginEndPath);
                btPlus.addEntry(entryNode);
            }
        }
        return btPlus;
    }

}
