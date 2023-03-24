package cn.edu.scut.priloc.web.servlet;

import Priloc.data.EncTrajectory;
import Priloc.data.Trajectory;
import Priloc.data.TrajectoryReader;
import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.mapper.Entry;
import cn.edu.scut.priloc.pojo.BeginEndPath;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.service.impl.EncTrajectoryServiceImpl;
import com.alibaba.fastjson.JSON;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.ParseException;
import java.util.List;

@WebServlet("/encTrajectory/*")
public class EncTrajectoryServlet extends BaseServlet{
    private EncTrajectoryService etlDsService = new EncTrajectoryServiceImpl();
    private HttpSession session;

    void selectByPage(HttpServletRequest request, HttpServletResponse response)
    {

    }

    void add(HttpServletRequest request,HttpServletResponse response) throws IOException {

        //从session域拿到密文轨迹
        session=request.getSession();
        EncTrajectory encTrajectory = (EncTrajectory) session.getAttribute("eTlds");

        etlDsService.add(encTrajectory);

        //EncTrajectory encTrajectory = JSON.parseObject(jsonString,EncTrajectory.class);

        etlDsService.add(encTrajectory);
    }
    void selectByETLDs(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //从session域拿到密文轨迹
        session=request.getSession();
        EncTrajectory encTrajectory = (EncTrajectory) session.getAttribute("eTlds");

        List<EncTrajectory> encTrajectories = etlDsService.selectByETLDs(encTrajectory);

        //调用同心圆树算法

    }



}
