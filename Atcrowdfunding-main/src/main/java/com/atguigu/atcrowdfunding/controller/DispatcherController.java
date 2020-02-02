package com.atguigu.atcrowdfunding.controller;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.manager.service.UserService;
import com.atguigu.atcrowdfunding.potal.service.MemberService;
import com.atguigu.atcrowdfunding.util.AjaxRusult;
import com.atguigu.atcrowdfunding.util.Const;
import com.atguigu.atcrowdfunding.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author wall
 * @data 20 - 22:02
 */
@Controller
public class DispatcherController {
    @Autowired
    private UserService userService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PermissionService permissionService;

    //    异步请求,第一个注解@ResponseBody将返回结果序列化成为一个json串，即result中包含是否成功，失败的失败消息
//    而登录操作我们没有把查到的数据放到result中，而是先放在session域
    @ResponseBody
    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public Object doLogin(String loginacct, String userpswd, String type, String rememberme,
                          HttpSession session, HttpServletResponse response) {
        AjaxRusult result = new AjaxRusult();
        try {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("loginacct", loginacct);
            paramMap.put("userpswd", MD5Util.digest(userpswd));
            paramMap.put("type", type);

            if ("member".equals(type)) {

                Member member = memberService.queryMemberLogin(paramMap);
                session.setAttribute(Const.LOGIN_MEMBER, member);

                if ("1".equals(rememberme)) {
                    String logincode = "loginacct=" + member.getLoginacct() + "&userpwd=" + member.getUserpswd() + "&logintype=member";
                    //loginacct=zhangsan&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=member
                    System.out.println("用户-存放到Cookie中的键值对：logincode::::::::::::" + logincode);

                    Cookie c = new Cookie("logincode", logincode);

                    c.setMaxAge(60 * 60 * 24 * 14); //2周时间Cookie过期     单位秒
                    c.setPath("/"); //表示任何请求路径都可以访问Cookie

                    response.addCookie(c);
                }

            } else if ("user".equals(type)) {

                User user = userService.queryUserLogin(paramMap);
                session.setAttribute(Const.LOGIN_USER, user);

                if ("1".equals(rememberme)) {
                    String logincode = "\"loginacct=" + user.getLoginacct() + "&userpwd=" + user.getUserpswd() + "&logintype=user\"";
                    //loginacct=superadmin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=user
                    System.out.println("用户-存放到Cookie中的键值对：logincode::::::::::::" + logincode);

                    Cookie c = new Cookie("logincode", logincode);

                    c.setMaxAge(60 * 60 * 24 * 14); //2周时间Cookie过期     单位秒
                    c.setPath("/"); //表示任何请求路径都可以访问Cookie

                    response.addCookie(c);
                }
                //-------------------------------------------------------
                List<Permission> myPermissions = permissionService.queryPermissionByUserid(user.getId());   //当前用户所拥有的许可
                Permission permissionRoot = null;
                Set<String> myUris = new HashSet<>();

                for (Permission outerpermission : myPermissions) {
                    myUris.add("/" + outerpermission.getUrl());
                    //通过子查找父
                    //子菜单
                  //  Permission child = permission; //假设为子菜单
                    if (outerpermission.getPid() == null) {
                        permissionRoot = outerpermission;
                    } else {
                        //父节点
                        for (Permission innerpermission : myPermissions) {
                            if (outerpermission.getPid() == innerpermission.getId()) {
                                Permission parent = innerpermission;
                                parent.getChildren().add(outerpermission);
                                break; //跳出内层循环,如果跳出外层循环,需要使用标签跳出
                            }
                        }
                    }
                }

                session.setAttribute(Const.MY_URIS, myUris);
                session.setAttribute("permissionRoot", permissionRoot);
                //-------------------------------------------------------

            } else {

            }
            result.setData(type);
            result.setSuccess(true);
        } catch (Exception e) {
            result.setMessage("亲,,，您的用户名或密码错误");
            e.printStackTrace();
            result.setSuccess(false);
        }
        return result;
    }

    //同步请求
   /* @RequestMapping("/doLogin")
    public String doLogin(String loginacct, String userpswd, String type, HttpSession session){
        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("loginacct",loginacct);
        paramMap.put("userpswd",userpswd);
        paramMap.put("type",type);
        User user = userService.queryUserLogin(paramMap);
        session.setAttribute(Const.LOGIN_USER,user);
        return "redirect:/main.htm";
    }*/
//    登陆成功后重定向到主页面防止表单重复提交
    @RequestMapping("/main")
    public String main() {
        return "main";
    }

    @RequestMapping("/member")
    public String member() {
        return "member/member";
    }


    @RequestMapping("/index")
    public String toIndex() {
        return "index";
    }

//此方法记住密码不是帮你登录而是回显账户密码等所有登录所需信息
   /* @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpSession session, Map<String, Object> paramMap) {

        Cookie[] cookies = request.getCookies();
        if (cookies != null) { //如果客户端禁用了Cookie，那么无法获取Cookie信息

            for (Cookie cookie : cookies) {
                if ("logincode".equals(cookie.getName())) {
                    String logincode = cookie.getValue();
                    System.out.println("获取到Cookie中的键值对" + cookie.getName() + "===== " + logincode);
                    //loginacct=admin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=member
                    String[] split = logincode.split("&");
                    if (split.length == 3) {
                        String loginacct = split[0].split("=")[1];
                        String userpwd = split[1].split("=")[1];
                        String logintype = split[2].split("=")[1];
                        paramMap.put("loginacct", loginacct);
                        paramMap.put("userpswd", userpwd);
                        paramMap.put("type", logintype);
                    }
                }
            }


        }

        System.out.println("走了控制器");
        return "login";
    }
*/

   @RequestMapping("/login")
   public String login(HttpServletRequest request, HttpSession session){

       //判断是否需要自动登录
       //如果之前登录过，cookie中存放了用户信息，需要获取cookie中的信息，并进行数据库的验证

       boolean needLogin = true;
       String logintype = null ;
       Cookie[] cookies = request.getCookies();
       if(cookies != null){ //如果客户端禁用了Cookie，那么无法获取Cookie信息

           for (Cookie cookie : cookies) {
               if("logincode".equals(cookie.getName())){
                   String logincode = cookie.getValue();
                   System.out.println("获取到Cookie中的键值对"+cookie.getName()+"===== " + logincode);
                   //loginacct=admin&userpwd=21232f297a57a5a743894a0e4a801fc3&logintype=member
                   String[] split = logincode.split("&");
                   if(split.length == 3){
                       String loginacct = split[0].split("=")[1];
                       String userpwd = split[1].split("=")[1];
                       logintype = split[2].split("=")[1];
                       Map<String,Object> paramMap = new HashMap<String,Object>();
                       paramMap.put("loginacct", loginacct);
                       paramMap.put("userpswd", userpwd);
                       paramMap.put("type", logintype);

                       if("user".equals(logintype)){

                           User dbLogin = userService.queryUserLogin(paramMap);

                           if(dbLogin!=null){
                               session.setAttribute(Const.LOGIN_USER, dbLogin);
                               needLogin = false ;
                           }

                           //加载当前登录用户的所拥有的许可权限.


                           List<Permission> myPermissions = permissionService.queryPermissionByUserid(dbLogin.getId());   //当前用户所拥有的许可
                           Permission permissionRoot=null;
                           Set<String> myUris = new HashSet<>();

                           for (Permission permission : myPermissions) {
                               myUris.add("/"+permission.getUrl());
                               //通过子查找父
                               //子菜单
                               Permission child = permission ; //假设为子菜单
                               if(child.getPid() == null ){
                                   permissionRoot=permission;
                               }else{
                                   //父节点
                                   for (Permission innerpermission : myPermissions) {
                                       if(child.getPid() == innerpermission.getId()){
                                           Permission parent = innerpermission;
                                           parent.getChildren().add(child);
                                           break ; //跳出内层循环,如果跳出外层循环,需要使用标签跳出
                                       }
                                   }
                               }
                           }

                           session.setAttribute(Const.MY_URIS,myUris);
                           session.setAttribute("permissionRoot",permissionRoot);

                           /*Map<Integer,Object> map=new HashMap<>();
                           Set<String> myUris=new HashSet<>();
                           Permission permissionRoot=null;
                           List<Permission> permissions = permissionService.queryAllPermission();
                           for (Permission permission : permissions) {
                               map.put(permission.getId(),permission);
                               myUris.add("/"+permission.getUrl());
                           }
                           for (Permission permission : permissions) {
                           Permission child=permission;
                              if(child.getPid()==null){
                                  permissionRoot=permission;
                              }else{
                                  Permission parent= (Permission) map.get(child.getPid());
                                  parent.getChildren().add(child);
                              }
                           }
                           session.setAttribute("permissionRoot", permissionRoot);
                           session.setAttribute(Const.MY_URIS, myUris);*/
//----------------------------------------------------------------------------------------------------------
                       }else if("member".equals(logintype)){

                           Member dbLogin = memberService.queryMemberLogin(paramMap);

                           if(dbLogin!=null){
                               session.setAttribute(Const.LOGIN_MEMBER, dbLogin);
                               needLogin = false ;
                           }
                       }

                   }
               }
           }
       }

       if(needLogin){
           return "login";
       }else{
           if("user".equals(logintype)){
               return "redirect:/main.htm";
           }else if("member".equals(logintype)){
               return "redirect:/member.htm";
           }
       }
       return "login";
   }


    @RequestMapping("/logout")
    public String logout(HttpSession session) {
//        销毁session对象或者清理session域
        session.invalidate();
        return "redirect:/index.htm";
    }


}





