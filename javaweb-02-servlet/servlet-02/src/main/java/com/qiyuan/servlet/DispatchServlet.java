package com.qiyuan.servlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName DispatcherServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/24 16:44
 * @Version 1.0
 **/
public class DispatchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        //RequestDispatcher dispatcher = context.getRequestDispatcher("/getp"); // 设置转发地址
        //dispatcher.forward(req,resp); // 将这里的参数转发出去
        context.getRequestDispatcher("/getp").forward(req,resp); // 一句话
    }
}
