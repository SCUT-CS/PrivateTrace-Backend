package cn.edu.scut.priloc.web.controller;

import cn.edu.scut.priloc.pojo.*;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.util.CoordinateTransformUtil;
import cn.edu.scut.priloc.util.TrajectoryReader;
import cn.edu.scut.priloc.util.TreeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/priloc")
public class doController {

    @Autowired
    private EncTrajectoryService eTldsService;

    @Value("${tree.temp-path}")
    private String tempPath;
    @GetMapping("/showAll")
    public List<tableData> showAll(){
        return eTldsService.showAll();
    }

    @GetMapping("/showByIndex")
    public EncTrajectory showByIndex(@RequestParam("index") Integer index){
        return eTldsService.showByIndex(index);
    }
    @PostMapping("/upload/{userId}")
    public Trajectory upload(
            @RequestBody MultipartFile multipartFile,
            @PathVariable String userId,
            HttpServletRequest request) throws IOException, ParseException {
        //从前端接收一个plt文件和用户名
        //获取明文轨迹
        System.out.println(request.getServletContext().getRealPath(""));
        //获取文件暂存路径
        String name=multipartFile.getOriginalFilename();
        String path=tempPath +"\\"+ name;
        multipartFile.transferTo(new File(path));
        File file = new File(path);
        TrajectoryReader reader=new TrajectoryReader(file);
        List<Trajectory> trajectoryList = reader.load(userId);
        Trajectory trajectory = trajectoryList.get(0);
        System.out.println(file.delete());

        //安全性考虑，传到session
        HttpSession session = request.getSession();
        session.setAttribute("tlds"+userId,trajectory);
        session.setAttribute("fileName"+userId,name);
        //坐标转换
        for (TimeLocationData tld : trajectory.getTlds()) {
            double[] bd09 = CoordinateTransformUtil.wgs84tobd09(tld.getLocation().getLongitude(), tld.getLocation().getLatitude());
            tld.getLocation().setLongitude(bd09[0]);
            tld.getLocation().setLatitude(bd09[1]);
        }
        return trajectory;
    }

    @GetMapping("/encrypt/{userId}")
    public EncTrajectory encrypt(
            HttpServletRequest request,
            @PathVariable String userId) throws IOException {
        HttpSession session = request.getSession();
        Trajectory trajectory = (Trajectory) session.getAttribute("tlds" + userId);
        //加密
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("加密");
        EncTrajectory encTrajectory = eTldsService.encrypt(trajectory);
        stopWatch.stop();
        session.setAttribute("etlds"+userId,encTrajectory);
        System.out.println(stopWatch.prettyPrint());
        //加密完成后删除明文轨迹
        return encTrajectory;
    }
    @RequestMapping("/query/{userId}")
    public boolean query(
            HttpServletRequest request,
            @PathVariable String userId) throws Exception{
        HttpSession session = request.getSession();
        EncTrajectory encTrajectory = (EncTrajectory) session.getAttribute("etlds" + userId);
        session.removeAttribute("etlds" + userId);
//        ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("E:\\GitHub\\PriTLD\\PriTLD\\DataBase\\001\\20081023234104.txt"));
//        EncTrajectory eTdls = (EncTrajectory) inputStream.readObject();
//        eTdls.setUserId("000");
        ArrayList<BeginEndPath> beginEndPathList = eTldsService.selectByETLDs(encTrajectory);
        System.out.println(beginEndPathList.size());
        List<EncTrajectory> encTrajectories = new ArrayList<>();
        for (BeginEndPath o : beginEndPathList) {
            //读入磁盘位置中的加密轨迹
            encTrajectories.add(TreeUtils.getETlds(o));
        }
        boolean flag = eTldsService.query(encTrajectories,encTrajectory);
        if(flag) {//如果判断交错，
            String name = (String) session.getAttribute("fileName" + userId);
            System.out.println(name);
            Trajectory trajectory = (Trajectory) session.getAttribute("tlds" + userId);
            //TODO 最后记得测试添加
            eTldsService.add(encTrajectory,trajectory,name);//添加到阳性库
        }
        session.setAttribute(userId+"list",beginEndPathList);
        return flag;
    }

    @GetMapping("/check/{userId}")
    public List<Trajectory> check(
            HttpServletRequest request,
            @PathVariable String userId) throws IOException, ClassNotFoundException {
        HttpSession session = request.getSession();
        ArrayList<BeginEndPath> beginEndPathList = (ArrayList<BeginEndPath>) session.getAttribute(userId + "list");
        Trajectory trajectory = (Trajectory) session.getAttribute("tlds" + userId);
        session.removeAttribute("tlds" + userId);
        session.removeAttribute(userId + "list");
        return eTldsService.getTrajectoryList(beginEndPathList,trajectory);
    }

}
