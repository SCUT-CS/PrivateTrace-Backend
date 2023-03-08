package cn.edu.scut.priloc.service;

import java.util.List;

public interface ETLDsService {
    List<eTLDs> selectByPage(int currentPage,int pageSize);

    void add(TLDs tlds);

    List<eTLDs> selectByTLDs(int beginTime,int endTime);
}
