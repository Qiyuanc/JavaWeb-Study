package com.qiyuan.test;

import java.sql.*;

/**
 * @ClassName TestJdbc
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/5 12:07
 * @Version 1.0
 **/
public class TestJDBC {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // useUnicode=true&characterEncoding=utf-8 设置中文编码
        String url = "jdbc:mysql://localhost:3306/jdbc?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
        String username = "root";
        String password = "0723";
        // 1.加载驱动
        Class.forName("com.mysql.jdbc.Driver");
        // 2.获取连接 connection对象代表了数据库
        Connection connection = DriverManager.getConnection(url, username, password);
        // 3.向数据库发送SQL语句的对象 statement : CRUD
        Statement statement = connection.createStatement();
        // 4.写SQL语句
        //String sql = "delete form users where id = 4";
        // 增删改都使用 executeUpdate 返回受影响的行数
        //int i = statement.executeUpdate(sql);
        String sql = "Select * from users";
        // 5.执行SQL查询，返回一个结果集ResultSet
        ResultSet rs = statement.executeQuery(sql);
        // 6.遍历结果集获取对象
        while(rs.next()){
            System.out.println("id:"+rs.getObject("id"));
            System.out.println("name:"+rs.getObject("name"));
            System.out.println("password:"+rs.getObject("password"));
            System.out.println("email:"+rs.getObject("email"));
            System.out.println("birthday:"+rs.getObject("birthday"));
        }
        // 7.关闭连接（必要），先开后关
        rs.close();
        statement.close();
        connection.close();
    }
}
