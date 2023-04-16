package cn.edu.scut.priloc.web.controller;

import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.Trajectory;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.util.TrajectoryReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/priloc")
public class doController {

    @Autowired
    private EncTrajectoryService eltdsService;
    @GetMapping("/upload")
    public Trajectory upload(
            @RequestBody MultipartFile multipartFile,
            @RequestParam String userId,
            HttpServletRequest request) throws IOException, ParseException {
        //从前端接收一个plt文件和用户名
        //获取明文轨迹
        File file = new File(multipartFile.getOriginalFilename());
        TrajectoryReader reader=new TrajectoryReader(file);
        Trajectory trajectory = reader.load(userId);
        file.delete();

        //安全性考虑，传到session
        request.getSession().setAttribute("tlds"+userId,trajectory);
        return trajectory;
    }

    @GetMapping("/encrypt/{userId}")
    public EncTrajectory encrypt(
            HttpServletRequest request,
            @PathVariable String userId){
        HttpSession session = request.getSession();
        Trajectory trajectory = (Trajectory) session.getAttribute("tlds" + userId);
        //加密
        EncTrajectory encTrajectory=new EncTrajectory(trajectory);
        session.setAttribute("etlds"+userId,encTrajectory);
        //加密完成后删除明文轨迹
        //请求转发到密文预览
        return encTrajectory;
    }
    @RequestMapping("/query")
    public List<EncTrajectory> query(@RequestBody EncTrajectory encTrajectory){
        List<EncTrajectory> encTrajectories = eltdsService.selectByETLDs(encTrajectory);

        //TODO 调用同心圆树算法

        //查询完成后添加到索引树
        eltdsService.add(encTrajectory);
        return encTrajectories;
    }
}
