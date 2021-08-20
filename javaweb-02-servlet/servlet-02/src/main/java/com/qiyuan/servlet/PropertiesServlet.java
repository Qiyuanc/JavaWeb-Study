package com.qiyuan.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName PropertiesServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/24 17:40
 * @Version 1.0
 **/
public class PropertiesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        InputStream in = context.getResourceAsStream("/WEB-INF/classes/db.properties"); // 以流形式获取资源文件
        Properties prop = new Properties();
        prop.load(in);
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        //设置编码
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("用户名："+username+"\n"+"密码："+password);
    }
}
