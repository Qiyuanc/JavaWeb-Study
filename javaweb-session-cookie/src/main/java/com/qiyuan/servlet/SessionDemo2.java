package com.qiyuan.servlet;

import com.qiyuan.pojo.Person;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName SessionDemo2
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/27 22:13
 * @Version 1.0
 **/
public class SessionDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        //获得Session
        HttpSession session = req.getSession();
        Person person = (Person) session.getAttribute("name");
        out.write(person.toString());
    }
}
