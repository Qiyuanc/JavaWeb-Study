package com.qiyuan.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @ClassName CharacterEncodingFilter
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/5 20:45
 * @Version 1.0
 **/
public class CharacterEncodingFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setCharacterEncoding("UTF-8");
        servletResponse.setCharacterEncoding("UTF-8");
        filterChain.doFilter(servletRequest, servletResponse);
    }

    public void destroy() {

    }
}
