package cn.edu.scut.priloc.web.servlet;

import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.service.imp.EncTrajectoryServiceImp;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/encTrajectory/*")
public class EncTrajectoryServlet extends BaseServlet{
    private EncTrajectoryService etlDsService = new EncTrajectoryServiceImp();

    void selectByPage(HttpServletRequest request, HttpServletResponse response)
    {

    }

    void add(HttpServletRequest request,HttpServletResponse response)
    {
        //先解密再调用
    }

}
