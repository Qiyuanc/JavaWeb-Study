package com.qiyuan.servlet;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @ClassName ImageServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/25 21:35
 * @Version 1.0
 **/
public class ImageServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //浏览器定时刷新
        resp.setHeader("refresh", "5");
        //在内存中创建一个图片
        BufferedImage image = new BufferedImage(80, 20, BufferedImage.TYPE_INT_RGB);
        //获取图片
        Graphics2D g = (Graphics2D) image.getGraphics();
        //设置背景,填充整个图片
        g.setColor(Color.white);
        g.fillRect(0, 0, 80, 20);
        //向图片写入随机数
        g.setColor(Color.blue);
        g.setFont(new Font(null,Font.BOLD,20));
        g.drawString(CreatNum(),0,20);

        //让浏览器以图片方式打开请求
        resp.setContentType("image/jpeg");
        //取消浏览器的缓存
        resp.setDateHeader("exprise",-1);
        resp.setHeader("Cache-Control","no-cache");
        resp.setHeader("Pragma","no-cache");

        //将图片写到浏览器
        ImageIO.write(image,"jpeg",resp.getOutputStream());
    }

    //生成随机数
    public String CreatNum() {
        Random random = new Random();
        String num = random.nextInt(9999) + "";
        StringBuffer sb = new StringBuffer();
        //不够四位的 补0
        for (int i = 0; i < 4-num.length(); i++) {
            sb.append("0");
        }
        num = sb.toString()+num;
        return num;
    }
}
