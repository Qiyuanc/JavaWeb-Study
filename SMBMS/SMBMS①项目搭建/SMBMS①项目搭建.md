## SMBMS项目搭建

开始进行SMBMS（超市订单管理系统）项目的分析。

### 1. SMBMS结构

<img src="F:\TyporaMD\SMBMS\SMBMS①项目搭建\image-20210805173344806.png" alt="image-20210805173344806" style="zoom: 80%;" />



### 2. 数据库模型

<img src="F:\TyporaMD\SMBMS\SMBMS①项目搭建\image-20210805173620415.png" alt="image-20210805173620415" style="zoom:80%;" />

- smbms_user：用户表
- smbms_role：职责表
- smbms_bill：订单表
- smbms_provider：供应商表
- smbms_address：地址表

### 3. 项目搭建准备

1. 创建Maven项目

   使用Webapp模板创建，或创建普通项目后添加Webapp框架。

2. 配置Tomcat，测试运行

3. 导入Maven依赖

   Servlet、jsp、jstl、standard、mysql...

4. 创建项目包结构

   com.qiyuan.servlet、entity、dao、filter、service、util...

5. 编写实体类

   ORM映射：表 -> 实体类 int -> Interger？

6. 编写基础公共类

   6.1 数据库配置文件db.properties

   ```properties
   driver=com.mysql.jdbc.Driver
   url=jdbc:mysql://localhost:3306?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
   username=root
   password=0723
   ```

   6.2 编写操作数据库基类

   ```java
   package com.qiyuan.dao;
   
   import java.io.IOException;
   import java.io.InputStream;
   import java.sql.*;
   import java.util.Properties;
   /**
    * @ClassName BaseDao
    * @Description TODO
    * @Author Qiyuan
    * @Date 2021/8/5 18:32
    * @Version 1.0
    **/
   public class BaseDao {
       private static String driver;
       private static String url;
       private static String username;
       private static String password;
       // 静态代码块，在类加载的时候就执行
       static {
           Properties properties = new Properties();
           // 通过类加载器获取对应的资源
           InputStream is = BaseDao.class.getClassLoader().getResourceAsStream("db.propertoes");
           try {
               properties.load(is);
           } catch (IOException e) {
               e.printStackTrace();
           }
           driver = properties.getProperty("driver");
           url = properties.getProperty("url");
           username = properties.getProperty("username");
           password = properties.getProperty("password");
       }
   
       // 获取数据库连接
       public static Connection getConnection() {
           Connection connection = null;
           try {
               Class.forName(driver);
               connection = DriverManager.getConnection(url, username, password);
           } catch (Exception e) {
               e.printStackTrace();
           }
           return connection;
       }
       /**
        * 查询数据库的公共操作
        * @param connection,sql,params,rs,ptsm
        */
       public static ResultSet execute(Connection connection, String sql, Object[] params, ResultSet rs, PreparedStatement pstm) {
           try {
               pstm = connection.prepareStatement(sql);
               for (int i = 0; i < params.length; i++) {
                   pstm.setObject(i + 1, params[i]);
               }
               rs = pstm.executeQuery();
           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }
           return rs;
       }
       /**
        * 更新数据库的公共操作
        * @param connection,sql,params,ptsm
        */
       public static int execute(Connection connection, String sql, Object[] params, PreparedStatement pstm) {
           int updateRows = 0;
           try {
               pstm = connection.prepareStatement(sql);
               for (int i = 0; i < params.length; i++) {
                   pstm.setObject(i + 1, params[i]);
               }
               updateRows = pstm.executeUpdate();
           } catch (SQLException throwables) {
               throwables.printStackTrace();
           }
           return updateRows;
       }
       /**
        * 释放资源
        * @param connection,pstm,rs
        */
       public static boolean closeResource(Connection connection, PreparedStatement pstm, ResultSet rs) {
           boolean flag = true;
           if (rs != null) {
               try {
                   rs.close();
                   rs = null;//GC回收
               } catch (SQLException e) {
                   e.printStackTrace();
                   flag = false;
               }
           }
           if (pstm != null) {
               try {
                   pstm.close();
                   pstm = null;//GC回收
               } catch (SQLException e) {
                   e.printStackTrace();
                   flag = false;
               }
           }
           if (connection != null) {
               try {
                   connection.close();
                   connection = null;//GC回收
               } catch (SQLException e) {
                   e.printStackTrace();
                   flag = false;
               }
           }
           return flag;
       }
   }
   ```

   6.3 编写字符编码过滤器

7. 导入静态资源

   css、js、img...

   

✨准备工作完成！

