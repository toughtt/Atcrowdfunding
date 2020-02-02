package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import com.atguigu.atcrowdfunding.potal.service.TicketService;
import com.atguigu.atcrowdfunding.util.AjaxRusult;
import com.atguigu.atcrowdfunding.util.Page;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wall
 * @data - 8:46
 */
@Controller
@RequestMapping("/authcert")
public class AuthcertController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private TicketService ticketService ;
    @Autowired
    private MemberService memberService ;




    @RequestMapping("/index")
    public String index() {
        return "authcert/index";
    }

    @RequestMapping("/show")
    public String show(Integer memberid,Map<String,Object> map){

        Member member = memberService.getMemberById(memberid);

        List<Map<String,Object>> list = memberService.queryCertByMemberid(memberid);

        map.put("member", member);
        map.put("certimgs", list);

        return "authcert/show";
    }



    @ResponseBody
    @RequestMapping("/pageQuery")
    public Object pageQuery(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                            @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize) {
        AjaxRusult result = new AjaxRusult();
        Page page=new Page(pageno,pagesize);
        
        try {
            //1.查询后台backuser委托组的任务
            TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKey("auth")
                    .taskCandidateGroup("backuser");
            List<Task> listPage = taskQuery.listPage(page.getStartIndex(), pagesize);

            List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();


            for (Task task : listPage) {
                Map<String,Object> map = new HashMap<String,Object>();
                map.put("taskid", task.getId());
                map.put("taskName", task.getName());

                //2.根据任务查出任务所属的流程定义（拿到流程定义名称，版本）
                ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
                        processDefinitionId(task.getProcessDefinitionId()).singleResult();

                map.put("procDefName", processDefinition.getName());

                map.put("procDefVersion", processDefinition.getVersion());


                //3.根据任务查询任务所属流程实例，再根据流程实例的id去t_ticket表流程单表 查询出对应的会员信息
                Member member = ticketService.getMemberByPiid(task.getProcessInstanceId());

                map.put("member",member);

                data.add(map);

            }
            page.setDatas(data);

            Long count = taskQuery.count();
            page.setTotalsize(count.intValue());

            result.setPage(page);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("查询任务列表失败");
        }

        return result;
    }


    @ResponseBody
    @RequestMapping("/pass")
    public Object pass( String taskid, Integer memberid ) {
        AjaxRusult result = new AjaxRusult();

        try {
            taskService.setVariable(taskid, "flag", true);
            taskService.setVariable(taskid, "memberid", memberid);
            // 传递参数，让流程继续执行
            taskService.complete(taskid);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

    @ResponseBody
    @RequestMapping("/refuse")
    public Object refuse(String taskid, Integer memberid) {
        AjaxRusult result = new AjaxRusult();

        try {
            taskService.setVariable(taskid, "flag", false);
            taskService.setVariable(taskid, "memberid", memberid);
            // 传递参数，让流程继续执行
            taskService.complete(taskid);
            result.setSuccess(true);
        } catch ( Exception e ) {
            e.printStackTrace();
            result.setSuccess(false);
        }

        return result;
    }

}