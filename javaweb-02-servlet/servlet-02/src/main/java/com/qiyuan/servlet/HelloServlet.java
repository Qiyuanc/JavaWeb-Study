package com.qiyuan.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName HelloServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/24 15:31
 * @Version 1.0
 **/
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //this.getInitParameter();  获取初始化参数 返回String
        //this.getServletConfig();  获取Servlet配置 返回ServletConfig
        ServletContext context = this.getServletContext();//获取Servlet上下文 返回ServletContext
        String username = "祈鸢";
        context.setAttribute("username",username); // 将一个数据保存在Servlet中，数据名"username" 数据值"祈鸢"
        System.out.println("Hello");
    }
}
