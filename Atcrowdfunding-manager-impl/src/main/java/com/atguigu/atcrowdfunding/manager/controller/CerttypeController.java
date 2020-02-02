package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.Cert;
import com.atguigu.atcrowdfunding.manager.service.CertService;
import com.atguigu.atcrowdfunding.manager.service.CerttypeService;
import com.atguigu.atcrowdfunding.util.AjaxRusult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wall
 * @data - 17:22
 */
@Controller
@RequestMapping("/certtype")
public class CerttypeController {
    @Autowired
    private CerttypeService certtypeService;
    @Autowired
    private CertService certService ;


    /*@RequestMapping("/index")
    public String index(){
        return "certtype/index";
    }*/
    @RequestMapping("/index")
    public String index(Map<String,Object> map){
        //查询所有资质
        List<Cert> queryAllCert = certService.queryAllCert();
        map.put("allCert", queryAllCert);


        //查询资质与账户类型之间关系(表示之前给账户类型分配过资质)
        List<Map<String,Object>> certAccttypeList =  certtypeService.queryCertAccttype();
        map.put("certAccttypeList", certAccttypeList);

        return "certtype/index";
    }



    @ResponseBody
    @RequestMapping("/insertAcctTypeCert")
    public Object insertAcctTypeCert( Integer certid, String accttype ) {
        AjaxRusult result = new AjaxRusult();

        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("certid", certid);
            paramMap.put("accttype", accttype);

            int count = certtypeService.insertAcctTypeCert(paramMap);
            result.setSuccess(count==1);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/deleteAcctTypeCert")
    public Object deleteAcctTypeCert( Integer certid, String accttype ) {
        AjaxRusult result = new AjaxRusult();

        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("certid", certid);
            paramMap.put("accttype", accttype);

            int count = certtypeService.deleteAcctTypeCert(paramMap);
            result.setSuccess(count==1);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }



}
