## SMBMS④密码修改

### 1. 数据传递过程

![image-20210807181429478](F:\TyporaMD\SMBMS\SMBMS④密码修改\image-20210807181429478.png)

再回顾一下程序中数据传递的顺序，从高层到底层；**编码顺序最好反向，从底层到高层**。

### 2. 密码修改实现

#### 2.1 导入前端页面

导入前端页面pwdmodify.jsp，注意：IDEA导入JSP后要使用Build -> Rebuild Project中重新部署项目，否则会找不到页面报404错误。

Tips： pwdmodify.jsp引用了pwdmodify.js代码，目前还看不懂😵。

#### 2.2 UserDao接口

```java
public interface UserDao {
    ...
    // 修改当前用户密码
    public int updatePwd(Connection connection, int id, String pwd) throws SQLException;
}
```

#### 2.3 UserDao接口实现类

负责对数据库的操作，修改数据库中的密码

```java
public class UserDaoImpl implements UserDao{ 
    ...
	public int updatePwd(Connection connection, int id, String pwd) throws SQLException {
        // 返回值作用域在外面
        int execute = 0;
        // 先判断连接是否为空，防止上层传入null导致异常
        if (connection!=null){
            // 准备参数
            PreparedStatement pstm = null;
            String sql = "update smbms_user set userPassword=? where id=?";
            Object[] param = {id,pwd};
            // 调用BaseDao中的更新方法
            execute = BaseDao.execute(connection, pstm, sql, param);
        }
        // 若返回值不为空，说明有行受到影响，即成功
        return execute;
    }
}
```

#### 2.4 UserService接口

```java
public interface UserService {
    ...
    // 根据用户ID修改密码
    public boolean updatePwd(int id,String pwd);
}
```

#### 2.5 UserService接口实现类

事务（业务）失败需要回滚，所以将Connection放在业务层，记得关闭连接

UserServiceImpl类中有一个UserDao对象供调用Dao层的方法

```java
public class UserServiceImpl implements UserService {
    // 业务层调用Dao层获取数据，需要引入Dao层
    private UserDao userDao;
    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }
    ...
    public boolean updatePwd(int id, String pwd) {
        // 要关闭连接，要提升作用域
        Connection connection = null;
        // 设置标记，判断修改是否成功
        boolean flag = false;
        try {
            // 关系到事务，在业务层创建连接往下传
            connection = BaseDao.getConnection();
            // 修改密码 业务
            if(userDao.updatePwd(connection, id, pwd)>0){
                flag = true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return flag;
    }
}
```

#### 2.6 UserServlet控制层

此处进行了Servlet复用，在前端提交请求时带上请求的方法

```jsp
<%--隐藏域中提交方法为savepwd--%>
<input type="hidden" name="method" value="savepwd">
```

把更新密码提取为一个方法updatePwd( req , resp)，在doGet中根据前端请求的方法参数选择不同的方法

```java
public class UserServlet extends HttpServlet {
    // Servlet复用，UserServlet不只修改密码，还要增删改查
    // 怎么在一个doGet中执行多个方法？
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("savepwd".equals(method)){
            this.updatePwd(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 从Session中获取用户信息
        Object object = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;
        if (object != null && !StringUtils.isNullOrEmpty(newpassword)){
            UserService userService = new UserServiceImpl();
            // 传递当前用户的ID 和前端的新密码 去修改 成功返回true
            flag = userService.updatePwd(((User) object).getId(), newpassword);
            if(flag){
                req.setAttribute("message","修改成功，请退出后再次登录");
                //密码修改完了 移除当前的Session 注销
                req.getSession().removeAttribute(Constants.USER_SESSION);
            }else{
                req.setAttribute("message","修改失败");
            }
        }else{
            req.setAttribute("message","新密码出错");
        }
        // 这里是什么情况？
        //req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        resp.sendRedirect(req.getContextPath()+"/jsp/pwdmodify.jsp");
    }
}
```

**遇到的问题**

1. 因为还没学习Ajax，旧密码验证还做不了，只能将pswmodify.js中的旧密码框的验证去掉。去掉后重启服务器页面却没有改变。

   **Solution**：浏览器清除缓存记录，或者使用强制刷新（Chrome中shift+F5），以确保重新加载了js配置。

2. 修改密码提示修改失败。

   **Solution**： UserDao里替换预编译语句的Object数组参数顺序反了，导致password替换了id，id替换了password，当然修改失败。

3. 修改完成后明明写了转发到pwdmodify.jsp，按理说此处移除了Session相当于注销，应该被过滤器跳到error页面的，却没有反应，为什么？

   **Solution**：我认为，转发是服务器内部行为，浏览器并没有发起请求，所以不会被过滤；将转发换成重定向后，改完密码就会跳到error页面了。



修改密码完成（暂时）！下一篇Ajax验证旧密码😏！
