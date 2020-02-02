package com.atguigu.atcrowdfunding.manager.controller;

import com.atguigu.atcrowdfunding.bean.Role;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.util.AjaxRusult;
import com.atguigu.atcrowdfunding.util.Page;
import com.atguigu.atcrowdfunding.util.StringUtil;
import com.atguigu.atcrowdfunding.vo.Data;
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
 * @data 20 - 11:51
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    //分配角色
    @ResponseBody
    @RequestMapping("/doAssignRole")
    public Object doAssignRole(Integer userid,Data data){
        AjaxRusult result = new AjaxRusult();
        try {

            userService.saveUserRoleRelationship(userid,data);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("分配角色数据失败!");
        }

        return result;
    }

    //取消分配角色
    @ResponseBody
    @RequestMapping("/doUnAssignRole")
    public Object doUnAssignRole(Integer userid,Data data){
        AjaxRusult result = new AjaxRusult();
        try {

            userService.deleteUserRoleRelationship(userid,data);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("取消分配角色数据失败!");
        }

        return result;
    }

    /*@RequestMapping("/assignRole")
    public String assignRole(){
        return "user/assignrole";
    }*/
    //显示分配页面数据.
    @RequestMapping("/assignRole")
    public String assignRole(Integer id,Map map){


        List<Role> allListRole = userService.querAllRole();

        List<Integer> roleIds = userService.queryRoleByUserid(id);


        List<Role> leftRoleList = new ArrayList<Role>(); //未分配角色
        List<Role> rightRoleList = new ArrayList<Role>(); //已分配角色


        for(Role role : allListRole){

            if(roleIds.contains(role.getId())){
                rightRoleList.add(role);
            }else{
                leftRoleList.add(role);
            }

        }

        map.put("leftRoleList", leftRoleList);
        map.put("rightRoleList", rightRoleList);

        return "user/assignrole";
    }

    @RequestMapping("/toUpdate")
    public String toUpdate(Integer id,Map map){
       User user = userService.getUserById(id);
        map.put("user",user);
        return "user/update";
    }
    @RequestMapping("/toAdd")
    public String toAdd(){
        return "user/add";
    }

    @RequestMapping("/index")
    public String index(){
        return "user/index";
    }

    //批量删除，接受多条数据组成的表单映射到实体对象，可用一个包含该实体对象集合的VO类来接受参数，页面封装参数为json对象时
//    可用josnObj["xxxList[i].username"]=...来封装成ajax的data属性值。
    @ResponseBody
    @RequestMapping("/doDeleteBatch")
    public Object doDeleteBatch(Data data) {
        AjaxRusult result = new AjaxRusult();
        try {
            int count = userService.deleteBatchUserByVo(data);
            result.setSuccess(count == data.getUserList().size());

        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("批量删除数据失败!");
        }

        return result;
    }

/*//    批量删除,接受一个带多个值的路径拼接字符串id=1&id=2&id=3，用一个同名也叫id的integer数组即可封装到Integer数组中
@ResponseBody
@RequestMapping("/doDeleteBatch")
public Object doDeleteBatch(Integer[] id){
    AjaxRusult result = new AjaxRusult();
    try {
        int count = userService.deleteBatchUser(id);
        result.setSuccess(count==id.length);

    } catch (Exception e) {
        result.setSuccess(false);
        e.printStackTrace();
        result.setMessage("批量删除数据失败!");
    }

    return result;
}*/

//    删除用户
        @ResponseBody
        @RequestMapping("/doDelete")
        public Object doDelete(Integer id){
            AjaxRusult result = new AjaxRusult();
            try {
                int count = userService.deleteUser(id);
                result.setSuccess(count==1);

            } catch (Exception e) {
                result.setSuccess(false);
                e.printStackTrace();
                result.setMessage("删除数据失败!");
            }

            return result;
        }

//    修改用户
    @ResponseBody
    @RequestMapping("/doUpdate")
    public Object doUpdate(User user){
        AjaxRusult result = new AjaxRusult();
        try {
            int count = userService.updateUser(user);
            result.setSuccess(count==1);

        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("更新数据失败!");
        }

        return result;
    }

//    添加用户
    @ResponseBody
    @RequestMapping("/doAdd")
    public Object doAdd(User user){
        AjaxRusult result = new AjaxRusult();
        try {
            int count = userService.saveUser(user);
            result.setSuccess(count==1);

        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("保存数据失败!");
        }

        return result;
    }


    //用户列表展示，条件查询
    @ResponseBody
    @RequestMapping("/doIndex")
    public Object doIndex(@RequestParam(value="pageno",required=false,defaultValue="1") Integer pageno,
                        @RequestParam(value="pagesize",required=false,defaultValue="10") Integer pagesize,
                        String queryText){
        AjaxRusult result = new AjaxRusult();
        try {
            Map<String,Object> paramMap = new HashMap<>();
            paramMap.put("pageno", pageno);
            paramMap.put("pagesize", pagesize);

            if(StringUtil.isNotEmpty(queryText)){

                if(queryText.contains("%")){
                    queryText = queryText.replaceAll("%", "\\\\%");
                }

                paramMap.put("queryText", queryText); //   \%
            }
            Page page = userService.queryPage(paramMap);
            result.setSuccess(true);
            result.setPage(page);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("查询数据失败!");
        }

        return result; //将对象序列化为JSON字符串,以流的形式返回.
    }


//    异步请求
   /* @ResponseBody
    @RequestMapping("/index")
    public Object index(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                        @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize){
        AjaxRusult result = new AjaxRusult();
        try {
            Page page = userService.queryPage(pageno,pagesize);
            result.setSuccess(true);
            result.setPage(page);
        } catch (Exception e) {
            result.setSuccess(false);
            e.printStackTrace();
            result.setMessage("查询数据失败");
        }
//异步请求到controller返回的不再是string类型的页面跳转，而是自己封装的一个对象包含查询数据和其他数据，框架会将该对象序列化成json串
        return result;

    }
*/

//    同步请求
    /*@RequestMapping("/index")
    public String index(@RequestParam(value = "pageno",required = false,defaultValue = "1") Integer pageno,
                        @RequestParam(value = "pagesize",required = false,defaultValue = "10") Integer pagesize,
                        Map map){
       Page page = userService.queryPage(pageno,pagesize);
       map.put("page",page);
       return "user/index-1";

    }*/


}
