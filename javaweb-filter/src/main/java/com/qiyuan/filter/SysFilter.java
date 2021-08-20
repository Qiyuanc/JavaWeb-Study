package com.qiyuan.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName SysFilter
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/3 23:01
 * @Version 1.0
 **/
public class SysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //ServletRequest是HttpServletRequest的父类 没有getSession方法 需要强转
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        Object user_session = req.getSession().getAttribute("USER_SESSION");
        if(user_session == null){
            resp.sendRedirect("/filter/error.jsp");
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
    public void destroy() {
    }
}
