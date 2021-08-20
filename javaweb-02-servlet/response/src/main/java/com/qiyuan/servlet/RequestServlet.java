package com.qiyuan.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName RequestServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/26 15:19
 * @Version 1.0
 **/
public class RequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入Request请求");
        //使用参数处理请求
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println(username+":"+password);
        //重定向页面->SUCCESS.jsp
        //重定向需要完整路径，转发是服务器内部跳转故不需要
        resp.sendRedirect("/r/success.jsp");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
