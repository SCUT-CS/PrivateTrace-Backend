package cn.edu.scut.priloc.util;

import cn.edu.scut.priloc.pojo.EncTimeLocationData;
import cn.edu.scut.priloc.pojo.TimeLocationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

@Component
public class EncryptDecryptUtil {

    protected  final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Async
    public  Future<ArrayList<EncTimeLocationData>> doEncrypt(List<TimeLocationData> tlds, int begin, int end) {
        logger.info("异步线程启动，between"+begin+" and "+end);
        ArrayList<EncTimeLocationData> eTlds=new ArrayList<>();
        for (int i = begin; i < end; i++) {
            eTlds.add(tlds.get(i).encrypt());
        }
        return new AsyncResult<>(eTlds);
    }

    @Async
    public  Future<ArrayList<TimeLocationData>> doDecrypt(List<EncTimeLocationData> eTlds, int begin, int end) {
        logger.info("异步线程启动，between"+begin+" and "+end);
        ArrayList<TimeLocationData> tlds=new ArrayList<>();
        for (int i = begin; i < end; i++) {
            tlds.add(eTlds.get(i).decrypt());
        }
        return new AsyncResult<>(tlds);
    }
}
