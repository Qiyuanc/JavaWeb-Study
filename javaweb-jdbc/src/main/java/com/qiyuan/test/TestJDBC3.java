package com.qiyuan.test;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @ClassName TestJDBC3
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/5 15:33
 * @Version 1.0
 **/
public class TestJDBC3 {
    @Test
    public void TestJunit() {
        // useUnicode=true&characterEncoding=utf-8 设置中文编码
        String url = "jdbc:mysql://localhost:3306/jdbc?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
        String username = "root";
        String password = "0723";
        // 在catch中用到了，需要先定义出来
        Connection connection = null;
        try {
            // 1.加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 2.获取连接 connection对象代表了数据库
            connection = DriverManager.getConnection(url, username, password);

            // 3.通知数据库开启事务，false为开启，即关闭自动提交
            connection.setAutoCommit(false);

            String sql = "update account set money = money-100 where name = 'A'";
            connection.prepareStatement(sql).executeUpdate();
            // 制造错误
            int i = 1 / 0;
            String sql2 = "update account set money = money+100 where name = 'B'";
            connection.prepareStatement(sql2).executeUpdate();

            connection.commit(); //提交事务
            System.out.println("事务提交！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 若上面出错，则数据库回滚
                connection.rollback();
                // 最后，关闭数据库连接
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
