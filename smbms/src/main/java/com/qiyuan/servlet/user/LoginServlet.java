package com.qiyuan.servlet.user;

import com.qiyuan.entity.User;
import com.qiyuan.service.user.UserServiceImpl;
import com.qiyuan.util.Constants;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName LoginServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/6 20:11
 * @Version 1.0
 **/
public class LoginServlet extends HttpServlet {
    // Servlet控制层，处理请求，调用业务层代码
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet Start");
        // 获取用户名和密码
        String userCode = req.getParameter("userCode");// 前端提交的参数名称
        String userPassword = req.getParameter("userPassword");
        // 和数据库的数据进行对比，调用业务
        UserServiceImpl userService = new UserServiceImpl();
        // 获取登录的用户
        User user = userService.login(userCode, userPassword);
        if( user != null){ //存在该用户
            // 将用户的信息放入Session中
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            // 重定向到主页
            resp.sendRedirect("jsp/frame.jsp");
        }else{ // 信息错误
            // 携带报错信息 ，前端通过error获取
            req.setAttribute("error","用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
