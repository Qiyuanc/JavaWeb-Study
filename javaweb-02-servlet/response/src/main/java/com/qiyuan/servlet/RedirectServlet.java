package com.qiyuan.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName RedirectServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/26 14:46
 * @Version 1.0
 **/
public class RedirectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //resp.setHeader("Location","/r/image");
        //resp.setStatus(302);
        resp.sendRedirect("/r/image"); // 重定向
    }
}
