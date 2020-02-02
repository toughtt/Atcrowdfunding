package com.atguigu.atcrowdfunding.manager.service.Impl;

import com.atguigu.atcrowdfunding.manager.dao.TestDao;
import com.atguigu.atcrowdfunding.manager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wall
 * @data - 12:39
 */
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestDao testDao;
    @Override
    public void insert() {
        Map map = new HashMap();
        map.put("name","zhangsan");
        testDao.insert(map);

    }
}
