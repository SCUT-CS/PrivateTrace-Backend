package cn.edu.scut.priloc.web.controller;

import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.Trajectory;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.util.TrajectoryReader;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/upload/{userId}")
    public Trajectory upload(
            @RequestBody MultipartFile multipartFile,
            @PathVariable String userId,
            HttpServletRequest request) throws IOException, ParseException {
        //从前端接收一个plt文件和用户名
        //获取明文轨迹
        System.out.println(request.getServletContext().getRealPath(""));
        //获取文件暂存路径
        String path=System.getProperty("user.dir")+"\\PriTLD\\temp/" + multipartFile.getOriginalFilename();
        System.out.println(path);
        multipartFile.transferTo(new File(path));
        File file = new File(path);
        TrajectoryReader reader=new TrajectoryReader(file);
        Trajectory trajectory = reader.load(userId);
        System.out.println(file.delete());

        //安全性考虑，传到session
        request.getSession().setAttribute("tlds"+userId,trajectory);
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
    public List<Trajectory> query(
            HttpServletRequest request,
            @PathVariable String userId) {
        EncTrajectory encTrajectory = (EncTrajectory) request.getSession().getAttribute("etlds" + userId);
        List<EncTrajectory> encTrajectories = eTldsService.selectByETLDs(encTrajectory);
        List<Trajectory> trajectoryList = new ArrayList<>();
        if(!eTldsService.query(encTrajectories,encTrajectory)){//如果判断交错，
            //eTldsService.add(encTrajectory);//添加到阳性库
            for (EncTrajectory eTld : encTrajectories) {//解密后发回前端
                trajectoryList.add(eTldsService.decrypt(eTld));
            }
        }
        return trajectoryList;//如果为空，说明没有交集
    }


}
