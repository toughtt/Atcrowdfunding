package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.manager.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wall
 * @data 20 - 12:32
 */
@Controller
public class TestController {
    @Autowired
    private TestService testService;   //依赖倒转原则

    @RequestMapping("/test")
    public String test(){
        System.out.println("TestController");
        testService.insert();
        return "success";
    }
}
