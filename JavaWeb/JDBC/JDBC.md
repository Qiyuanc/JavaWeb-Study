## JDBC复习

### 1. 什么是JDBC

Java数据库连接，（Java Database Connectivity，简称JDBC）是Java语言中用来规范客户端程序如何来访问数据库的应用程序接口，提供了诸如查询和更新数据库中数据的方法。

JDBC在应用程序和数据库之间的关系

![image-20210804113029551](F:\TyporaMD\JavaWeb\JDBC\image-20210804113029551.png)

### 2. 使用JDBC

在Maven中导入数据库的依赖

```xml
<!--Mysql的驱动-->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.22</version>
</dependency>
```

在IDEA中连接数据库

<img src="F:\TyporaMD\JavaWeb\JDBC\image-20210805125754852.png" alt="image-20210805125754852" style="zoom:67%;" />

Tips：mysql 连接错误The server time zone value

解决方案：在连接字符串后面加上**?serverTimezone=UTC**，设置时区

#### 2.1 JDBC基本步骤

1. 加载驱动com.mysql.jdbc.Driver
2. 获取连接，connection对象代表了数据库
3. 向数据库发送SQL语句的对象statement ，进行CRUD操作
4. 写SQL语句
5. 执行SQL查询，返回一个结果集对象ResultSet
6. 遍历结果集获取数据
7. 关闭连接（必要），先开后关

```java
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
        String sql = "Select * from users";
        // 5.执行SQL查询，返回一个结果集对象ResultSet
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
```

增删改使用executeUpdate(sql)，返回受影响的行数

```java
        String sql = "delete form users where id = 4";
        // 增删改都使用 executeUpdate 返回受影响的行数
        int i = statement.executeUpdate(sql);
```

#### 2.2 使用预编译的陈述

```java
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
        ps.setInt(1, 4); // 给第一个预占符赋值为 int型 值为4
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

```

### 3. JDBC事务

#### 3.1 什么是事务

在人员管理系统中，删除一个人员需要删除人员的基本资料，也要删除和该人员相关的信息，如信箱，文章等，这些数据库操作语句就构成一个事务。

#### 3.2 事务的4个条件（ACID）

- **原子性（Atomicity）**：事务中的操作要么全部成功，要么全部失败。
- **一致性（Consistency）**：事务必须使数据库从一个一致性状态变换到另外一个一致性状态。
- **隔离性（Isolation）**：事务独立运行，不能影响其他事务或被其他事务影响。
- **持久性（Durability）**：一个事务一旦被提交，它对数据库中数据的改变就是永久性的。

#### 3.3 Junit单元测试（+）

添加Maven依赖

```xml
<!--Junit单元测试-->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.7.2</version>
    <scope>test</scope>
</dependency>
```

在需要测试的**方法**上加上@Test注解

```java
public class TestJDBC3 {
    @Test
    public void TestJunit(){
        System.out.println("Hello,World");
    }
}
```

这个方法就可以被单独运行；可以同时测试多个方法。

#### 3.4 事务测试

在控制台中执行以下操作，通过事务修改数据

```mysql
START TRANSACTION ; #开启事务
update account set money = money-100 where name = 'A';
update account set money = money+100 where name = 'B';
rollback ; #回滚
commit ; #提交
```

**注意**：若直接执行Line2，数据库会发生改变；若先执行Line1开启事务再执行Line2，此时数据库不会发生改变，必须等到事务对应的commit执行才会改变。

使用JDBC实现上面的操作

```java
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
```

在这段代码中，若

1. 使用connection.setAutoCommit(false)开启事务，且制造了错误

   则sql1语句因为没有commit，所以不会执行到数据库中，保持了一致性。

2. 没有使用connection.setAutoCommit(false)开启事务，且制造了错误

   则sql1语句会直接执行到数据库中，但因为错误导致程序中断，sql2无法执行，最后的结果是A减少了100元，但B没有加100元，违反了一致性。


JDBC复习到此结束🦈！（暂时）
