package com.qiyuan.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName GetServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/24 16:09
 * @Version 1.0
 **/
public class GetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletContext context = this.getServletContext(); // 获取的ServletContext与HelloServlet中的是同一个
        String username = (String) context.getAttribute("username"); // 程序不知道该数据是什么类型 需要强转
        //设置编码
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("用户名：" + username);
        //流程： 进入Hello页面会存放一个数据，再进入Get页面会将这个数据打印出来
    }
}
