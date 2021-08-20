package com.qiyuan.test;

import java.sql.*;

/**
 * @ClassName TestJdbc2
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/5 13:10
 * @Version 1.0
 **/
public class TestJDBC2 {
    public static void main(String[] args) throws Exception {
        // useUnicode=true&characterEncoding=utf-8 设置中文编码
        String url = "jdbc:mysql://localhost:3306/jdbc?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
        String username = "root";
        String password = "0723";
        // 1.加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 2.获取连接 connection对象代表了数据库
        Connection connection = DriverManager.getConnection(url, username, password);
        // 3.使用预编译的 preparedStatement，先写sql语句
        String sql = "insert into users(id,name,password,email,birthday) values(?,?,?,?,?) ";
        // 4.获取预编译对象
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, 5); // 给第一个预占符赋值为 int型 值为4
        ps.setString(2, "祈鸢c"); // 给第二个预占符赋值为 Stirng
        ps.setString(3, "0723"); // ...
        ps.setString(4, "0723@qq.com"); // ...
        // 给第五个预占符赋值为 Date，先用java.util.Date获取时间，再转换为java.sql.Date
        ps.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
        // 5.执行SQL语句
        int i = ps.executeUpdate();
        if (i > 0) {
            System.out.println("插入成功");
        }
        // 6.关闭连接（必要），先开后关
        ps.close();
        connection.close();
    }
}
