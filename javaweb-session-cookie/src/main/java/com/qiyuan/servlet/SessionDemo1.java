package com.qiyuan.servlet;

import com.qiyuan.pojo.Person;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName SessionDemo1
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/27 21:46
 * @Version 1.0
 **/
public class SessionDemo1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        //获得Session
        HttpSession session = req.getSession();
        //向Session中存数据
        session.setAttribute("name",new Person("祈鸢",20));
        //获取Session的ID
        String id = session.getId();
        //判断Session是不是新创建的
        if(session.isNew()){
            out.write("Session创建成功,ID:"+session.getId());
        }else {
            out.write("Session已存在,ID:"+session.getId());
        }
        //Session创建时做了什么事情
        //Cookie jsessionid = new Cookie("JSESSIONID", id);
        //resp.addCookie(jsessionid);
    }
}
