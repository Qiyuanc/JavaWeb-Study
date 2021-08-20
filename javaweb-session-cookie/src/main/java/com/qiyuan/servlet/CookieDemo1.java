package com.qiyuan.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

/**
 * @ClassName CookieDemo1
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/27 18:08
 * @Version 1.0
 **/
public class CookieDemo1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        //获取输出对象
        PrintWriter out = resp.getWriter();
        //从客户端请求中获取Cookie
        Cookie[] cookies = req.getCookies();
        //是否存在Cookie，两种情况
        if(cookies!=null){
            out.write("曾经的访问时间：");
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                //避免空指针异常
                if("lastLoginTime".equals(cookie.getName())){
                    //获取Cookie中的值并转换输出
                    //Long.parseLong(String)方法，将string参数解析为有符号十进制，返回一个long的result基本类型值
                    long lastLoginTime = Long.parseLong(cookie.getValue());
                    Date date = new Date(lastLoginTime);
                    out.write(String.valueOf(date));
                }
            }
        }else{
            out.write("第一次访问！");
        }
        //System.currentTimeMillis() 自公元时间以来的毫秒数，时间戳
        Cookie timeCookie = new Cookie("lastLoginTime", System.currentTimeMillis() + "");
        //设置Cookie的有效期 此处为一天
        timeCookie.setMaxAge(24*60*60);
        resp.addCookie(timeCookie);
    }
}
