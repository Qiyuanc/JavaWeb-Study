package com.qiyuan.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName HelloServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/20 16:58
 * @Version 1.0
 **/
public class HelloServlet extends HttpServlet {

    // doGet 和 doPost 只是实现请求的不同方式，可以相互调用，业务逻辑都一样
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();// 响应流
        writer.print("<h1>Hello,Servlet</h1>");

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
