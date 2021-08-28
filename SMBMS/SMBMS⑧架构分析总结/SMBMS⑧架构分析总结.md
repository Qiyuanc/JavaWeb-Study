## SMBMS⑧架构分析总结

### 1. SBMBS架构分析

经过之前的学习和实践，对SMBMS这个项目的结构已经有了比较清晰的理解了。在这再次总结一下SMBMS项目各个功能的执行流程。

![image-20210810170309025](F:\TyporaMD\SMBMS\SMBMS⑧架构分析总结\image-20210810170309025.png)

以用户管理的增删改查为例，进入管理页面就要先查询所有的信息，这是最基础的，然后在查询出的信息上进行增删改操作。增删改查的操作无不与三层结构有关，即Servlet、Service、Dao层，这三层一层调用一层，虽然繁杂但也已经将各个功能分离的很清晰了。

如图，增删改三个操作对应了三个发起请求的方式直接发起请求user.do、异步请求ajax、标签链接herf。

之前已经实现了用户管理页面的查询，在这再实现一个用户的增加操作，熟悉一下与查不同的增，而删改与增差不多，就直接略过，进入下一阶段了。

### 2. 增加用户

#### 2.1 导入前端页面

先导入前端页面useradd.jsp，反正也看不太懂不再赘述，知道是带着表单数据发起请求的就行了。

##### 2.1.1 AJAX验证用户名

添加用户时，输入的用户名（userCode）不能与数据库中的重复，此处就可以使用ajax来发送请求实时验证输入的用户名是否存在于数据库中

```js
// 用户名输入框失去焦点触发
userCode.bind("blur",function(){
		// ajax后台验证 userCode是否已存在
		// user.do?method=ucexist&userCode=**
		$.ajax({
			type:"GET",//请求类型
			url:path+"/jsp/user.do",//请求的url
			data:{method:"ucexist",userCode:userCode.val()},//请求参数
			dataType:"json",//ajax接口（请求url）返回的数据类型
            ...
     });
```

同时在UserServlet中添加userCodeExist方法，判断用户名是否存在

```java
    // 查看用户名是否可用
    private void userCodeExist(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //判断用户账号是否可用
        String userCode = request.getParameter("userCode");

        HashMap<String, String> resultMap = new HashMap<String, String>();
        if (StringUtils.isNullOrEmpty(userCode)) {
            //userCode == null || userCode.equals("")
            resultMap.put("userCode", "exist");
        } else {
            UserService userService = new UserServiceImpl();
            User user = userService.selectUserCodeExist(userCode);
            if (null != user) {
                resultMap.put("userCode", "exist");
                System.out.println("exist");
            } else {
                resultMap.put("userCode", "notexist");
                System.out.println("notexist");
            }
        }
        //把resultMap转为json字符串以json的形式输出
        //配置上下文的输出类型
        response.setContentType("application/json");
        //从response对象中获取往外输出的writer对象
        PrintWriter writer = response.getWriter();
        //把resultMap转为json字符串 输出
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();//刷新
        writer.close();//关闭流
    }
	//不要忘记在doGet中做好对应
```

同时要在UserService中添加selectUserCodeExist方法调用UserDao中的getLoginUser方法处理业务（UserDao中的getLoginUser方法查询不到用户会返回空，即用户名未使用，使代码复用；不复用UserService中的login方法的原因是login方法中需要对密码进行判断，而查询用户是否存在用不到密码），对比数据库判断用户名是否被使用，与login方法类似，此处省略。

这个方法通过三层架构「 Servlet -> UserService -> UserDao -> 数据库 」的路线判断用户名是否被使用。

##### 2.1.2 AJAX请求角色列表

在useradd.jsp中，选择添加的用户的角色时需要获取角色列表，这个请求由ajax发起

```js
$.ajax({
		type:"GET",//请求类型
		url:path+"/jsp/user.do",//请求的url
		data:{method:"getrolelist"},//请求参数
		dataType:"json",//ajax接口（请求url）返回的数据类型
    	...
    });
```

所以要在UserServlet中添加getRoleList方法，使前端能获取参数

```java
    // 获取角色列表的请求
    private void getRoleList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Role> roleList = null;
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        //把roleList转换成json对象输出
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.write(JSONArray.toJSONString(roleList));
        writer.flush();
        writer.close();
    }
	//不要忘记在doGet中做好对应
```

这个方法通过「 Servlet -> RoleService -> RoleDao -> 数据库 」的路线获取角色列表的数据。这些过程其实都在一遍遍的走这条路线。

这样前端的页面表单就能完整的接收数据了。

#### 2.1 UserDao接口

返回受影响的行数，若大于0则表明有行受到影响，说明操作成功。

```java
public interface UserDao {
    ...
    // 增加用户
    public int add(Connection connection, User user) throws Exception;
}
```

#### 2.2 UserDao接口实现类

```java
public class UserDaoImpl implements UserDao{
    ...
	public int add(Connection connection, User user) throws Exception {
        PreparedStatement pstm = null;
        // 返回值，受影响的行数
        int updateRows = 0;
        if (null != connection) {
            String sql = "insert into smbms_user (userCode,userName,userPassword," +
                    "userRole,gender,birthday,phone,address,creationDate,createdBy) " +
                    "values(?,?,?,?,?,?,?,?,?,?)";
            Object[] params = {user.getUserCode(), user.getUserName(), user.getUserPassword(),
                    user.getUserRole(), user.getGender(), user.getBirthday(),
                    user.getPhone(), user.getAddress(), user.getCreationDate(), user.getCreatedBy()};
            // 增删改 不需要结果集 rs
            updateRows = BaseDao.execute(connection, pstm, sql, params);
            BaseDao.closeResource(null, pstm, null);
        }
        return updateRows;
    }
}
```

#### 2.3 UserService接口

在Service层中就使用UserDao中add的int返回值对数据操作是否成功进行判断了，所以此处直接返回boolean表明是否成功即可。

```java
public interface UserService {
	...
    // 增加用户
    public boolean add(User user);
}
```

#### 2.4 UserService接口实现类

增删改与查询不同，涉及到了对数据库的数据修改，所以要使用事务保证数据一致性。

```java
public class UserServiceImpl implements UserService {
    // 业务层调用Dao层获取数据，需要引入Dao层
    private UserDao userDao;
    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }
    ...    
	public boolean add(User user) {
        // 返回值标记 作用域提升
        boolean flag = false;
        Connection connection = null;
        try {
            connection = BaseDao.getConnection();
            // 开启JDBC事务管理
            connection.setAutoCommit(false);
            int updateRows = userDao.add(connection, user);
            connection.commit();
            if (updateRows > 0) {
                // 添加成功
                flag = true;
                System.out.println("add success!");
            } else {
                // 添加失败
                System.out.println("add failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                System.out.println("========rollback========");
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            // 在service层进行connection连接的关闭
            BaseDao.closeResource(connection, null, null);
        }
        return flag;
    }

    @Test
    public void addTest(){
        UserServiceImpl userService = new UserServiceImpl();
        User user = new User();
		user.set(...);
        ...
        System.out.println(userService.add(user));
    }
}
```

在业务层测试一下这个业务是否能执行，方便后面改其他错误。

#### 2.5 UserServlet处理请求

UserServlet中接收到前端发来的method为add的请求，就调用add方法，并把req和resp参数传进去。

```java
public class UserServlet extends HttpServlet {
    // 总是忘记在这加导航
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if ("savepwd".equals(method)) {
            this.updatePwd(req, resp);
        } else if ("pwdmodify".equals(method)) {
            this.pwdModify(req, resp);
        }else if("query".equals(method)){
            this.query(req, resp);
        }else if("add".equals(method)){
            this.add(req, resp);
        }else if("getrolelist".equals(method)){
            this.getRoleList(req, resp);
        }else if("ucexist".equals(method)){
            this.userCodeExist(req, resp);
        }
    }
    ...
	// 增加用户
    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userCode = request.getParameter("userCode");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
        String gender = request.getParameter("gender");
        String birthday = request.getParameter("birthday");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String userRole = request.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setAddress(address);
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setGender(Integer.valueOf(gender));
        user.setPhone(phone);
        user.setUserRole(Integer.valueOf(userRole));
        // 直接创建一个Date就行了？不过好像是格林尼治时间
        user.setCreationDate(new Date());
        // 由当前用户创建的
        user.setCreatedBy(((User) request.getSession().getAttribute(Constants.USER_SESSION)).getId());

        // 万事俱备 添加！
        UserService userService = new UserServiceImpl();
        if (userService.add(user)) {
            response.sendRedirect(request.getContextPath() + "/jsp/user.do?method=query");
        } else {
            request.getRequestDispatcher("useradd.jsp").forward(request, response);
        }
    }
}
```

经过几条路线，添加用户的操作总算是完成了，比查询麻烦了不少，主要是有花里胡哨的操作。

### 3. 总结

研究了好几天这个SMBMS项目，从一窍不通到勉强看的明白JSP到JS到Servlet的逻辑关系了。主要的三层架构路线Servlet、Service、Dao也算比较熟悉了（虽然写的不多）。这个项目也就分析到这里了，后面还有两个文件上传和邮件发送以后再说吧，直接开始学习框架了，学完再回来重构这个项目。

到这JavaWeb基础也就结束了，如果忘记了还是会回来看看的😹。

*21/08/10 風栖祈鸢*

> *so leben wir und nehmen immer Abschied.*
>
> *我们就这样*,*生活在此地*并不断离别。
