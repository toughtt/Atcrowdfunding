package com.atguigu.atcrowdfunding.listener;

import com.atguigu.atcrowdfunding.bean.Permission;
import com.atguigu.atcrowdfunding.manager.service.PermissionService;
import com.atguigu.atcrowdfunding.util.Const;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author wall
 * @data 20 - 21:37
 */
public class StartSystermListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        //将应用的上下文路径request.getContextPath() 放到application域中
        ServletContext servletContext = sce.getServletContext();
        String contextPath = servletContext.getContextPath();
        servletContext.setAttribute("APP_PATH",contextPath);

        //加载全许可列表
        WebApplicationContext ioc = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        PermissionService permissionService = ioc.getBean(PermissionService.class);

        List<Permission> queryAllPermission = permissionService.queryAllPermission();

        Set<String> allURIs = new HashSet<String>();

        for (Permission permission : queryAllPermission) {
            allURIs.add("/"+permission.getUrl());
        }
        servletContext.setAttribute(Const.ALL_PERMISSION_URI, allURIs);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
