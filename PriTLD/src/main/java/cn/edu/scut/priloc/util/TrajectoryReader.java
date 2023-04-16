package cn.edu.scut.priloc.util;

import Priloc.geo.Location;
import cn.edu.scut.priloc.pojo.TimeLocationData;
import cn.edu.scut.priloc.pojo.Trajectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class TrajectoryReader {

    private String path;
    private Scanner scanner;
    public TrajectoryReader (File file) throws FileNotFoundException {
        this.path=file.getPath();
        scanner=new Scanner(file);
        for (int i = 0; i < 6; i++) {
            scanner.nextLine();
        }
    }
    public TrajectoryReader(String path) throws FileNotFoundException {
        this.path=path;
        this.scanner=new TrajectoryReader(new File(path)).getScanner();
    }
    public Trajectory load(String userId) throws ParseException {
        List<TimeLocationData> tlds = new ArrayList<>();
        while(scanner.hasNext()){
            String[] tokens = scanner.next().split(",");
            String dateString = tokens[5]+" "+tokens[6];
            SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(dateString);
            Location location = new Location(Double.parseDouble(tokens[0]),Double.parseDouble(tokens[1]));
            tlds.add(new TimeLocationData(location,date));
        }
        scanner.close();
        return new Trajectory(tlds,null,userId);
    }

    public Scanner getScanner() {
        return scanner;
    }
}

