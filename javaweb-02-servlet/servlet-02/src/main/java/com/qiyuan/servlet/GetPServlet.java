package com.qiyuan.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName GetPServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/24 16:36
 * @Version 1.0
 **/
public class GetPServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        String url = context.getInitParameter("url"); //xml中对应的名字
        resp.getWriter().print(url);
    }
}
