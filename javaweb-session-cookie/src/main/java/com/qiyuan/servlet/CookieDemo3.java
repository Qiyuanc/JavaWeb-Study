package com.qiyuan.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;

import static java.lang.System.out;

/**
 * @ClassName CookieDemo3
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/27 21:02
 * @Version 1.0
 **/
public class CookieDemo3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");

        Cookie[] cookies = req.getCookies();
        PrintWriter out = resp.getWriter();
        //遍历获取需要的Cookie
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            //避免空指针异常
            if("name".equals(cookie.getName())){
                //System.out.println(cookie.getValue());
                //out.write(cookie.getValue());
                //解码接收
                out.write(URLDecoder.decode(cookie.getValue(),"UTF-8"));
            }
        }
        //编码发送
        Cookie cncookie = new Cookie("name", URLEncoder.encode("祈鸢","UTF-8"));
        resp.addCookie(cncookie);
    }
}
