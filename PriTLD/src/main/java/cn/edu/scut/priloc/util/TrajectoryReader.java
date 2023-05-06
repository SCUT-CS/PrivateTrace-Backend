package cn.edu.scut.priloc.util;

import Priloc.geo.Location;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.pojo.TimeLocationData;
import cn.edu.scut.priloc.pojo.Trajectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TrajectoryReader {

    private static final String[] dateStrings;

    SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    static {
        dateStrings = new String[]{//把轨迹限定在两天以内
                "2008-10-24 ",
                "2008-10-25 "};
    }

    private final Scanner scanner;
    public TrajectoryReader (File file) throws FileNotFoundException {
        scanner=new Scanner(file);
        for (int i = 0; i < 6; i++) {
            scanner.nextLine();
        }
    }
    public TrajectoryReader(String path) throws FileNotFoundException {
        this.scanner=new TrajectoryReader(new File(path)).getScanner();
    }

    public List<Trajectory> load(String userId) throws ParseException {
        String lastDateString=null;
        boolean flag = false;//是否跨过一天的标志
        List<TimeLocationData> tlds = new ArrayList<>();
        while(scanner.hasNext()){
            String[] tokens = scanner.next().split(",");
            String dateString = tokens[5]+" "+tokens[6];
            //判断是否跨过一天
            if(lastDateString!=null&&!tokens[5].equals(lastDateString)) flag = true;//跨过一天
            lastDateString=tokens[5];
            Location location = new Location(Double.parseDouble(tokens[0]),Double.parseDouble(tokens[1]));
            tlds.add(new TimeLocationData(location,dateFormat.parse(dateString)));
        }
        //System.out.println(flag);
        scanner.close();
        Random random= new Random();
        String dateString;
        if (!flag)//没有跨一天，则随机选择一天
            dateString= dateStrings[random.nextInt(2)];
        else dateString=dateStrings[0];
        lastDateString=null;
        List<Trajectory> tempList = new ArrayList<>();
        List<TimeLocationData> tempTlds = new ArrayList<>();
        for (int i = 0; i < tlds.size(); i++){
            TimeLocationData tld = tlds.get(i);
            String date = dateFormat.format(tld.getDate()).split(" ")[0];//获取日期字符串
            if(lastDateString!=null&&!date.equals(lastDateString)){
                dateString=dateStrings[1];
            }
            String timeString = dateFormat.format(tld.getDate()).split(" ")[1];//获取时间字符串
            tld.setDate(dateFormat.parse(dateString+timeString));
            if(i>0){
                TimeLocationData lastTld = tlds.get(i - 1);
                //判断轨迹断点
                if (Duration.between(lastTld.getDate().toInstant(),tld.getDate().toInstant())
                        .compareTo(Duration.of(30, ChronoUnit.SECONDS))>0){
                    //System.out.println(tempTlds);
                    tempList.add(new Trajectory(tempTlds,userId));
                    tempTlds=new ArrayList<>();
                }
            }
            tempTlds.add(tld);
            lastDateString=date;
        }
        tempList.add(new Trajectory(tempTlds,userId));
        return tempList;
    }

    public Trajectory loadWithBep(BeginEndPath beginEndPath) {
        List<TimeLocationData> tlds = new ArrayList<>();
        while(scanner.hasNext()){
            String[] tokens = scanner.next().split(",");
            Location location = new Location(Double.parseDouble(tokens[0]),Double.parseDouble(tokens[1]));
            //只需验证地点
            tlds.add(new TimeLocationData(location,null));
        }
        return new Trajectory(tlds, beginEndPath.getUserId());
    }
    public Scanner getScanner() {
        return scanner;
    }
}

