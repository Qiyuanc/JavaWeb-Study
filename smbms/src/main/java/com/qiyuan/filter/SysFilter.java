package com.qiyuan.filter;

import com.qiyuan.entity.User;
import com.qiyuan.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName SysFilter
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/7 14:13
 * @Version 1.0
 **/
public class SysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 从Session中获取登录信息
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        // 已注销或未登陆
        if( user == null){
            System.out.println("重定向");
            // 加上项目路径
            response.sendRedirect("/smbms/error.jsp");
        }else { // 已登录 放行
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    public void destroy() {
    }
}
