package cn.edu.scut.priloc.web.servlet;

import cn.edu.scut.priloc.pojo.EncTrajectory;
import cn.edu.scut.priloc.pojo.TimeLocationData;
import cn.edu.scut.priloc.pojo.Trajectory;
import cn.edu.scut.priloc.util.TrajectoryReader;
import com.alibaba.fastjson.JSON;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@WebServlet("/trajectory/*")
public class TrajectoryServlet extends  BaseServlet{
    private HttpSession session;
    void encrypt(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Trajectory trajectory = (Trajectory) request.getAttribute("tlds");
        //加密
        EncTrajectory encTrajectory=new EncTrajectory(trajectory);
        //加密完成后删除明文轨迹
        session.removeAttribute("tlds");
        //请求转发到密文预览
        request.setAttribute("eTlds",encTrajectory);
        request.getRequestDispatcher("/encTrajectory/preview").forward(request,response);

    }
    void previewFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Trajectory trajectory = (Trajectory) request.getAttribute("tlds");
        List<TimeLocationData> tlds = trajectory.getTlds();
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
        Trajectory trajectory = reader.load(userId);

        //安全性考虑，传到session
        session = request.getSession();
        session.setAttribute("tlds",trajectory);

        //String tldsJson = JSON.toJSONString(trajectory);
        //String eTldsJson = JSON.toJSONString(encTrajectory);
        //response.addCookie(new Cookie("tlds",tldsJson));
        //response.addCookie(new Cookie("eTlds",eTldsJson));

        /*response.getWriter().write(eTldsJson);
        response.getWriter().write(tldsJson);*/
    }
}
