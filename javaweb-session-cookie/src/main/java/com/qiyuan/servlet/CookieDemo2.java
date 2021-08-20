package com.qiyuan.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName CookieDemo2
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/27 20:54
 * @Version 1.0
 **/
public class CookieDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建一个Cookie，名字要和被删除的一样
        Cookie timeCookie = new Cookie("lastLoginTime", System.currentTimeMillis() + "");
        //生命周期为0，立即失效
        timeCookie.setMaxAge(0);
        resp.addCookie(timeCookie);
    }
}
