package com.atguigu.atcrowdfunding.interceptor;

import com.atguigu.atcrowdfunding.bean.Member;
import com.atguigu.atcrowdfunding.bean.User;
import com.atguigu.atcrowdfunding.exception.LoginFailException;
import com.atguigu.atcrowdfunding.util.AjaxRusult;
import com.atguigu.atcrowdfunding.util.Const;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.Set;

/**
 * @author wall
 * @data - 17:11
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        判断是否是公开地址，哪些路径不需要拦截（白名单）,所以先设置白名单
        //1.定义哪些路径是不需要拦截(将这些路径称为白名单)
        Set<String> uri = new HashSet<String>();
        uri.add("/user/reg.do");
        uri.add("/user/reg.htm");
        uri.add("/login.htm");
        uri.add("/doLogin.do");
        uri.add("/logout.do");
        uri.add("/index.htm");

        //获取请求路径.
        String servletPath = request.getServletPath();

        if(uri.contains(servletPath)){
            return true ;
        }

        //2.判断用户是否登录,如果登录就放行
        HttpSession session = request.getSession();
        User user = (User)session.getAttribute(Const.LOGIN_USER);
        Member member = (Member)session.getAttribute(Const.LOGIN_MEMBER);

        if(user!=null || member!=null){
            return true ;
        }else{
            response.sendRedirect(request.getContextPath()+"/login.htm");
            request.getSession().setAttribute("message","对不起，请先登录");
            return false;
          /* throw new LoginFailException("对不起，请先登录");*/
        }

    }

}
