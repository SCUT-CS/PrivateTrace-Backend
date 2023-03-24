package cn.edu.scut.priloc.web.servlet;

import Priloc.data.*;
import cn.edu.scut.priloc.service.TrajectoryService;
import cn.edu.scut.priloc.service.impl.TrajectoryImpl;
import com.alibaba.fastjson.JSON;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@WebServlet("/trajectory/*")
public class TrajectoryServlet extends  BaseServlet{
    private TrajectoryService trajectoryService =new TrajectoryImpl();
    private HttpSession session;
    void encryptAndPreview(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Trajectory trajectory = (Trajectory) request.getAttribute("tlds");
        //加密
        EncTrajectory encTrajectory=new EncTrajectory(trajectory);
        List<EncTmLocData> eTlds = encTrajectory.geteTLDs();
        String eTldsJson = JSON.toJSONString(eTlds);
        response.getWriter().write(eTldsJson);

        //加密完成后删除明文轨迹
        session=request.getSession();
        session.removeAttribute("tlds");
        request.setAttribute("eTlds",encTrajectory);
    }
    void previewFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Trajectory trajectory = (Trajectory) request.getAttribute("tlds");
        List<TimeLocationData> tlds = trajectory.getTLDs();
        String tldsJson = JSON.toJSONString(tlds);
        response.getWriter().write(tldsJson);
    }

    void upload(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
        //先加密再调用

        //从前端接收一个plt文件和用户名
        String userId = request.getParameter("userId");
        File file=new File("");
        //获取明文轨迹
        TrajectoryReader reader=new TrajectoryReader(file);
        Trajectory trajectory = reader.load();

        //安全性考虑，传到session
        session = request.getSession();
        session.setAttribute("tlds",trajectory);

        String tldsJson = JSON.toJSONString(trajectory);
        //String eTldsJson = JSON.toJSONString(encTrajectory);
        response.addCookie(new Cookie("tlds",tldsJson));
        //response.addCookie(new Cookie("eTlds",eTldsJson));

        /*response.getWriter().write(eTldsJson);
        response.getWriter().write(tldsJson);*/
        /*
        其实可以在这里一步到位完成查询、添加的功能，但为了后期功能拓展，还是功能分开
        //先查询
        List<EncTrajectory> etlDs = etlDsService.selectByETLDs(bPlusTree, encTrajectory);
        //添加到索引树中
        etlDsService.add(bPlusTree,encTrajectory);
        */
    }
}
