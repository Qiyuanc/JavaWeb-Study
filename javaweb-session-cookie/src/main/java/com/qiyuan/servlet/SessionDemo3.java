package com.qiyuan.servlet;

import org.apache.jasper.runtime.HttpJspBase;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @ClassName SessionDemo3
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/27 22:26
 * @Version 1.0
 **/
public class SessionDemo3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        //删除Session中的信息
        session.removeAttribute("name");
        //手动注销Seesion，或者在web.xml中配置
        session.invalidate();
    }
}
