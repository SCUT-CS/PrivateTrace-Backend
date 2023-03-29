package cn.edu.scut.priloc.web.servlet;

import cn.edu.scut.priloc.pojo.*;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.service.impl.EncTrajectoryServiceImpl;
import com.alibaba.fastjson.JSON;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/encTrajectory/*")
public class EncTrajectoryServlet extends BaseServlet{
    private EncTrajectoryService etlDsService = new EncTrajectoryServiceImpl();
    private HttpSession session;

    void selectByPage(HttpServletRequest request, HttpServletResponse response)
    {

    }

    void preview(HttpServletRequest request,HttpServletResponse response) throws IOException {
        EncTrajectory eTlds = (EncTrajectory) request.getAttribute("eTlds");
        String eTldsJson = JSON.toJSONString(eTlds);
        response.getWriter().write(eTldsJson);
    }
    void add(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //需不需要分开暂定
        //从session域拿到密文轨迹
        session=request.getSession();
        EncTrajectory encTrajectory = (EncTrajectory) session.getAttribute("eTlds");



        //EncTrajectory encTrajectory = JSON.parseObject(jsonString,EncTrajectory.class);
    }
    void selectByETLDs(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //从session域拿到密文轨迹
        session=request.getSession();
        EncTrajectory encTrajectory = (EncTrajectory) session.getAttribute("eTlds");

        List<EncTrajectory> encTrajectories = etlDsService.selectByETLDs(encTrajectory);

        //调用同心圆树算法

        //查询完成后添加到索引树
        etlDsService.add(encTrajectory);
    }



}
