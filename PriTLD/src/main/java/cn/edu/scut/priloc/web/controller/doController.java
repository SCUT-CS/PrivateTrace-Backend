package cn.edu.scut.priloc.web.controller;

import Priloc.area.basic.EncryptedCircle;
import Priloc.data.EncTmLocData;
import Priloc.data.TimeLocationData;
import Priloc.protocol.CCircleTree;
import cn.edu.scut.priloc.pojo.EncTimeLocationData;
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
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/priloc")
public class doController {

    @Autowired
    private EncTrajectoryService eltdsService;
    @GetMapping("/upload/{userId}")
    public Trajectory upload(
            @RequestBody MultipartFile multipartFile,
            @PathVariable String userId,
            HttpServletRequest request) throws IOException, ParseException {
        //从前端接收一个plt文件和用户名
        //获取明文轨迹
        System.out.println(multipartFile.getName());
        System.out.println(request.getServletContext().getRealPath(""));
        String path="E:\\GitHub\\PriTLD\\PriTLD\\temp/" + multipartFile.getOriginalFilename();
        multipartFile.transferTo(new File("E:\\GitHub\\PriTLD\\PriTLD\\temp/" + multipartFile.getOriginalFilename()));
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
    public List<EncTrajectory> query(@RequestBody EncTrajectory encTrajectory) {
        List<EncTrajectory> encTrajectories = eltdsService.selectByETLDs(encTrajectory);
        List<Priloc.data.EncTrajectory> encCircles = new ArrayList<>();
        for (EncTrajectory encTrajectory1 : encTrajectories) {
            Priloc.data.EncTrajectory temp = getEncTrajectory(encTrajectory1);
            encCircles.add(temp);
        }
        // 在EncTrajectory.java中添加了一个构造函数 然后install
//    public EncTrajectory(List<EncTmLocData> eTLDs) {
//            this.eTLDs = eTLDs;
//        }
        CCircleTree cCircleTree = new CCircleTree();
        for (int i = 0; i < encCircles.size(); i++) {
            cCircleTree.add(encCircles.get(i));
        }
        try {
            cCircleTree.init(true);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        boolean result = cCircleTree.compare(getEncTrajectory(encTrajectory));
        //查询完成后添加到索引树
        eltdsService.add(encTrajectory);
        return encTrajectories;
    }

    private static Priloc.data.EncTrajectory getEncTrajectory(EncTrajectory encTrajectory1) {
        List<EncTimeLocationData> eTlds2 = encTrajectory1.geteTlds();
        List<EncTmLocData> eTLDs = new ArrayList();
        EncTmLocData pETLD = null;
        for (int i = 0; i < eTlds2.size(); ++i) {
            EncTmLocData cETLD = new EncTmLocData(new EncryptedCircle(eTlds2.get(i).getEncPoint(), 200), eTlds2.get(i).getDate());
            if (pETLD != null) {
                pETLD.setNext(cETLD);
            }
            cETLD.setPrevious(pETLD);
            eTLDs.add(cETLD);
            pETLD = cETLD;
        }
        Priloc.data.EncTrajectory temp = new Priloc.data.EncTrajectory(eTLDs);
        return temp;
    }
}
