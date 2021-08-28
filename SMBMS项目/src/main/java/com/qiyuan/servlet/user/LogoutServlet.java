package com.qiyuan.servlet.user;

import com.qiyuan.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LogoutServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/7 13:56
 * @Version 1.0
 **/
public class LogoutServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 移除Session
        req.getSession().removeAttribute(Constants.USER_SESSION);
        // 不加上项目路径，会走到localhost:8080/smbms/jsp/login.jsp
        resp.sendRedirect(req.getContextPath()+"/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
