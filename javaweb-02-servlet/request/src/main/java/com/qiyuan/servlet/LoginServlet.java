package com.qiyuan.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * @ClassName LoginServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/26 18:01
 * @Version 1.0
 **/
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码,避免控制台信息乱码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        //获取表单提交的信息
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String[] hobbies = req.getParameterValues("hobbies");
        //打印出来看看
        System.out.println(username);
        System.out.println(password);
        System.out.println(Arrays.toString(hobbies));
        //通过请求转发
        //req.getRequestDispatcher(req.getContextPath()+"/success.jsp").forward(req,resp);
        //重定向需要完整路径，转发是服务器内部跳转故不需要
        req.getRequestDispatcher("/success.jsp").forward(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
