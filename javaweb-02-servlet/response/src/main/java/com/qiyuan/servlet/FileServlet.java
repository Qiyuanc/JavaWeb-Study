package com.qiyuan.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * @ClassName FileServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/7/25 20:12
 * @Version 1.0
 **/
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 获取下载文件的路径 
        //String realPath = this.getServletContext().getRealPath("/1.jpg"); //不行
        String realPath = "F:\\IntelliJ IDEAProject\\javaweb\\javaweb-02-servlet\\response\\target\\classes\\艾拉.jpg";
        System.out.println("下载路径："+realPath);
        //2. 获取下载的文件名
        String filename = realPath.substring(realPath.lastIndexOf("\\") + 1);
        //3. 设置浏览器支持下载(Content-Disposition),使用URLEncoder转换中文编码，否则中文显示为 _
        resp.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(filename,"UTF-8"));
        //4. 获取下载文件的输入流
        FileInputStream in = new FileInputStream(realPath);
        //5. 创建缓冲区
        int len = 0;
        byte[] buffer = new byte[1024];
        //6. 获取OutptStream对象
        ServletOutputStream out = resp.getOutputStream();
        //7. 将FileOutputStream流写到Buffer缓冲区
        //8. 用OutptStream将缓冲区数据输出到浏览器
        while((len=in.read(buffer))>0){
            out.write(buffer,0,len);
        }
        // 关闭IO流
        in.close();
        out.close();
    }
}
