package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.util.AjaxRusult;
import com.atguigu.atcrowdfunding.util.Page;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * @author wall
 * @data - 21:41
 */
@Controller
@RequestMapping("/process")
public class ProcessController {


    @Autowired
    RepositoryService repositoryService;


    //点击菜单栏流程管理跳到index页面
    @RequestMapping("/index")
    public String index(){
        return "process/index";
    }

    @RequestMapping("/showimg")
    public String showimg(){
        return "process/showimg";
    }




    //跳到页面后马上发起异步请求分页查询“流程定义”做展示
    @ResponseBody
    @RequestMapping("/doIndex")
    public Object doIndex(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
                          @RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize){

        AjaxRusult result = new AjaxRusult();

        try {
            Page page = new Page(pageno,pagesize);

            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            //查询流程定义集合数据,可能出现了自关联,导致Jackson组件无法将集合序列化为JSON串.
            List<ProcessDefinition> listPage = processDefinitionQuery.listPage(page.getStartIndex(), pagesize);

            List<Map<String,Object>> mylistPage = new ArrayList<Map<String,Object>>();

            for (ProcessDefinition processDefinition : listPage) {

                Map<String,Object> pd = new HashMap<String,Object>();
                pd.put("id", processDefinition.getId());
                pd.put("name", processDefinition.getName());
                pd.put("key", processDefinition.getKey());
                pd.put("version", processDefinition.getVersion());

                mylistPage.add(pd);
            }


            Long totalsize = processDefinitionQuery.count();

            page.setDatas(mylistPage);

            page.setTotalsize(totalsize.intValue());

            result.setPage(page);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("查询流程定义失败!");
            e.printStackTrace();
        }

        return result;
    }
    /*@ResponseBody
    @RequestMapping("/deploy")
    public Object deploy(HttpServletRequest request,HttpSession session){

        AjaxRusult result = new AjaxRusult();

        try {
            MultipartHttpServletRequest req=( MultipartHttpServletRequest)request;
            MultipartFile mf=req.getFile("processDefFile");
            String originalFilename = mf.getOriginalFilename();
            String originalPath=originalFilename.substring(originalFilename.lastIndexOf("."));
            String path=session.getServletContext().getRealPath("/")+"\\dep\\"+ UUID.randomUUID().toString()+originalPath;
            mf.transferTo(new File(path));


            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("部署流程定义失败!");
            e.printStackTrace();
        }

        return result;
    }*/

    //点击上传流程定义 会弹出文件域选择 文件域发生改变change事件会将上传的文件通过流读取并部署 成功后会再次查询第一页的流程定义
    @ResponseBody
    @RequestMapping("/deploy")
    public Object deploy(HttpServletRequest request){

        AjaxRusult result = new AjaxRusult();

        try {
            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request;

            MultipartFile multipartFile  = multipartHttpServletRequest.getFile("processDefFile");

            repositoryService.createDeployment()
                    .addInputStream(multipartFile.getOriginalFilename(), multipartFile.getInputStream())
                    .deploy();

            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setMessage("部署流程定义失败!");
            e.printStackTrace();
        }
        return result;
    }

//删除流程定义，点击每一条后面的X 触发deleteProDef(id,name)事件，发起异步请求调repositoryService的级联删除方法
    //完成删除后再次查询第一页数据,传参id是流程定义对象的id
@ResponseBody
@RequestMapping("/doDelete")
public Object doDelete(String id){

    AjaxRusult result = new AjaxRusult();

    try {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();
        repositoryService.deleteDeployment(processDefinition.getDeploymentId(),true);    //true表示级联删除.

        result.setSuccess(true);
    } catch (Exception e) {
        result.setSuccess(false);
        result.setMessage("删除流程定义失败!");
        e.printStackTrace();
    }
    return result;
}

    @ResponseBody
    @RequestMapping("/showimgProDef")
    public void showimgProDef(String id, HttpServletResponse response) throws IOException {	 //流程定义id
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(id).singleResult();

        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());

        ServletOutputStream outputStream = response.getOutputStream();

        IOUtils.copy(resourceAsStream, outputStream);
    }


}
