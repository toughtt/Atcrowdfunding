package com.atguigu.atcrowdfunding.manager.service.Impl;

import com.atguigu.atcrowdfunding.manager.dao.AccountTypeCertMapper;
import com.atguigu.atcrowdfunding.manager.service.CerttypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wall
 * @data - 17:26
 */
@Service
public class CerttypeServiceImpl implements CerttypeService {
    @Autowired
    private AccountTypeCertMapper accountTypeCertMapper;

    @Override
    public List<Map<String, Object>> queryCertAccttype() {
        return accountTypeCertMapper.queryCertAccttype();
    }

    @Override
    public int insertAcctTypeCert(Map<String, Object> paramMap) {
        return accountTypeCertMapper.insertAcctTypeCert(paramMap);
    }

    @Override
    public int deleteAcctTypeCert(Map<String, Object> paramMap) {
        return accountTypeCertMapper.deleteAcctTypeCert(paramMap);
    }
}
