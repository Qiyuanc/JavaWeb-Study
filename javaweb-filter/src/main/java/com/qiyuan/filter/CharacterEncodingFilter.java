package com.qiyuan.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @ClassName CharacterEncodingFilter
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/3 14:21
 * @Version 1.0
 **/
public class CharacterEncodingFilter implements Filter {
    //初始化，Web服务器启动就初始化了
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("CharacterEncodingFilter初始化");
    }
    /*1.过滤器中的所有代码，在过滤特定请求（在web.xml中配置）的时候都会执行
      2.必须让过滤器放行请求 filterChain.doFilter(servletRequest,servletResponse);
    * */
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        servletResponse.setContentType("text/html;charset=UTF-8");
        //filterChain 过滤器链
        System.out.println("过滤前");
        //通过 doFilter 放行请求，若没有 doFilter 程序就在此被拦截
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("过滤后");
    }
    //销毁，Web服务器关闭时销毁
    public void destroy() {
        System.out.println("CharacterEncodingFilter销毁");
    }
}
