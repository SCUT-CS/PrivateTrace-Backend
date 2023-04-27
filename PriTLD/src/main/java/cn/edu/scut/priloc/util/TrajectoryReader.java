package cn.edu.scut.priloc.util;

import Priloc.geo.Location;
import cn.edu.scut.priloc.pojo.TimeLocationData;
import cn.edu.scut.priloc.pojo.Trajectory;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TrajectoryReader {

    private static String[] dateStrings;

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

    public Trajectory load(String userId) throws ParseException {
        String lastDate=null;
        boolean flag = false;//是否跨过一天的标志
        List<TimeLocationData> tlds = new ArrayList<>();
        while(scanner.hasNext()){
            String[] tokens = scanner.next().split(",");
            String dateString = tokens[5]+" "+tokens[6];
            //判断是否跨过一天
            if(lastDate!=null&&!tokens[5].equals(lastDate)) flag = true;//跨过一天
            lastDate=tokens[5];
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
        lastDate=null;
        for (TimeLocationData tld : tlds) {
            String date = dateFormat.format(tld.getDate()).split(" ")[0];//获取日期字符串
            if(lastDate!=null&&!date.equals(lastDate)){
                dateString=dateStrings[1];
            }
            lastDate=date;
            String timeString = dateFormat.format(tld.getDate()).split(" ")[1];//获取时间字符串
            tld.setDate(dateFormat.parse(dateString+timeString));
        }
        return new Trajectory(tlds,userId);
    }

    public Scanner getScanner() {
        return scanner;
    }
}

