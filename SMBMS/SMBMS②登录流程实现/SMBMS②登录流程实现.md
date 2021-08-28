## SMBMS②登录流程实现

### 1. 登录流程

![image-20210805215636998](F:\TyporaMD\SMBMS\SMBMS②登录流程实现\image-20210805215636998.png)

### 2. 代码编写流程

#### 2.1 前端页面login.jsp

分析项目，就不写JSP了...直接Ctrl+CV了，下面所有的JSP同理。

#### 2.2 设置首页

```xml
<welcome-file-list>
    <welcome-file>login.jsp</welcome-file>
</welcome-file-list>
```

#### 2.3 Dao层用户登录的接口

```java
public interface UserDao {
    public User getLoginUser(Connection connection,String userCode) throws SQLException;
}
```

#### 2.4 用户登录接口的实现类

```java
public class UserDaoImpl implements UserDao{
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if (null != connection) {
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
        }
        BaseDao.closeResource(null, pstm, rs);
        return user;
    }
}
```

#### 2.5 业务层用户登录的接口

```java
public interface UserService {
    // 用户登录
    public User login(String username,String password);
}
```

#### 2.6 用户登录业务的实现类

```java
/**
 * service层捕获异常，进行事务处理
 * 事务处理：调用不同dao的多个方法，必须使用同一个connection（connection作为参数传递）
 * 事务完成之后，需要在service层进行connection的关闭，在dao层关闭PreparedStatement和ResultSet对象
 */
public class UserServiceImpl implements UserService {
    // 业务层调用Dao层获取数据，需要引入Dao层
    private UserDao userDao;
    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }

    public User login(String userCode, String password) {
        Connection connection = null;
        User user = null;
        try {
            // 通过业务层调用对应的具体数据库操作
            connection = BaseDao.getConnection();
            // 获取登录用户的信息
            user = userDao.getLoginUser(connection, userCode);
            System.out.println(user.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return user;
    }
}
```

注意：获取不到用户信息，user为空后面可能会产生空指针错误。

#### 2.7 控制层Servlet处理请求

```java
public class LoginServlet extends HttpServlet {
    // Servlet控制层，处理请求，调用业务层代码
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("LoginServlet Start");
        // 获取用户名和密码
        String userCode = req.getParameter("userCode");// 前端提交的参数名称
        String userPassword = req.getParameter("userPassword");
        // 和数据库的数据进行对比，调用业务
        UserServiceImpl userService = new UserServiceImpl();
        // 获取登录的用户
        User user = userService.login(userCode, userPassword);
        if( user != null){ //存在该用户
            // 将用户的信息放入Session中
            req.getSession().setAttribute(Constants.USER_SESSION,user);
            // 重定向到主页
            resp.sendRedirect("jsp/frame.jsp");
        }else{ // 信息错误
            // 携带报错信息 ，前端通过error获取
            req.setAttribute("error","用户名或密码错误");
            req.getRequestDispatcher("login.jsp").forward(req,resp);
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

#### 2.8 注册Servlet

注意：注册Servlet的路径一定要和前端提交的请求路径相同，不要忘记。此处前端提交的路径是/login.do。

```xml
<servlet>
    <servlet-name>LoginServlet</servlet-name>
    <servlet-class>com.qiyuan.servlet.user.LoginServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>LoginServlet</servlet-name>
    <url-pattern>/login.do</url-pattern>
</servlet-mapping>
```

#### 2.9 登录测试，访问主页成功！

整个登录模块的执行流程

![image-20210806210209072](F:\TyporaMD\SMBMS\SMBMS②登录流程实现\image-20210806210209072.png)

登录模块基本搭建完成😊！

