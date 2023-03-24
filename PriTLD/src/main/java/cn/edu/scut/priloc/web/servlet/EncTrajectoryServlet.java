package cn.edu.scut.priloc.web.servlet;

import Priloc.data.EncTrajectory;
import Priloc.data.Trajectory;
import Priloc.data.TrajectoryReader;
import cn.edu.scut.priloc.mapper.BTreePlus;
import cn.edu.scut.priloc.mapper.Entry;
import cn.edu.scut.priloc.service.EncTrajectoryService;
import cn.edu.scut.priloc.service.impl.EncTrajectoryServiceImpl;
import com.alibaba.fastjson.JSON;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;

@WebServlet("/encTrajectory/*")
public class EncTrajectoryServlet extends BaseServlet{
    private EncTrajectoryService etlDsService = new EncTrajectoryServiceImpl();
    private  static BTreePlus bTreePlus;
    static {
        //从磁盘中读取索引树
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(""));
            bTreePlus = (BTreePlus) inputStream.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    void selectByPage(HttpServletRequest request, HttpServletResponse response)
    {

    }

    void add(HttpServletRequest request,HttpServletResponse response) throws IOException {
        BufferedReader reader = request.getReader();
        String jsonString = reader.readLine();

        EncTrajectory encTrajectory = JSON.parseObject(jsonString,EncTrajectory.class);

        etlDsService.add(bTreePlus,encTrajectory);
    }
    void selectByETLDs(HttpServletRequest request,HttpServletResponse response) throws IOException {
        //从前端接收
        BufferedReader reader = request.getReader();
        String jsonString = reader.readLine();

        EncTrajectory encTrajectory = JSON.parseObject(jsonString,EncTrajectory.class);

        etlDsService.selectByETLDs(bTreePlus,encTrajectory);

    }

    void upload(HttpServletRequest request,HttpServletResponse response) throws IOException, ParseException {
        //先加密再调用

        //从前端接收一个文件
        File file=new File("");
        TrajectoryReader reader=new TrajectoryReader(file);
        Trajectory trajectory = reader.load();
        //添加到索引树上
        pojo.BeginEndPath beginEndPath=new pojo.BeginEndPath(trajectory);
        Entry<pojo.BeginEndPath> entry=new Entry<>(beginEndPath.getBeginTime(), beginEndPath);
        bTreePlus.addEntry(entry);
        //加密
        EncTrajectory encTrajectory=new EncTrajectory(trajectory);


        //传回前端
        String jsonString = JSON.toJSONString(encTrajectory);
        response.getWriter().write(jsonString);
        /*
        其实可以在这里一步到位完成查询、添加的功能，但为了后期功能拓展，还是功能分开
        //先查询
        List<EncTrajectory> etlDs = etlDsService.selectByETLDs(bPlusTree, encTrajectory);
        //添加到索引树中
        etlDsService.add(bPlusTree,encTrajectory);
        */
    }

}
