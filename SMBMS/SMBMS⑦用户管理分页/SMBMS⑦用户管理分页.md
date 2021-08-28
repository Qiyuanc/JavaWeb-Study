## SMBMS⑦用户管理分页

### 1. 获取角色列表

为了职责统一，之前的用户相关操作都在各个包下的user内，如dao.user，service.user。同理，此处与角色相关的操作都放在各个报下的role内，如dao.role，service.role，与每个entity对应。

#### 1.1 RoleDao接口

```java
public interface RoleDao {
    // 获取角色列表
     public List<Role> getRoleList(Connection connection) throws Exception;
}
```

#### 1.2 RoleDao接口实现类

```java
public class RoleDaoImpl implements RoleDao {
    // 纯手打 都打腻了
    public List<Role> getRoleList(Connection connection) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        List<Role> list = new ArrayList<Role>();
        if (connection != null) {
            String sql = "select * from smbms_role";
            Object[] params = {};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            while(rs.next()){
                Role _role = new Role();
                _role.setId(rs.getInt("id"));
                _role.setRoleCode(rs.getString("roleCode"));
                _role.setRoleName(rs.getString("roleName"));
                list.add(_role);
            }
            BaseDao.closeResource(null,pstm,rs);
        }
        return list;
    }
}
```

#### 1.3 RoleService接口

```java
public interface RoleService {
    //获取角色列表
    public List<Role> getRoleList();
}
```

#### 1.4 RoleService接口实现类

```java
public class RoleServiceImpl implements RoleService {
    // 调用Dao层，要引入一个Dao层对象
    private RoleDao roleDao;
    public RoleServiceImpl() {
        roleDao = new RoleDaoImpl();
    }

    public List<Role> getRoleList() {
        // 要关闭连接，要提升作用域
        Connection connection = null;
        // 设置返回值
        List<Role> roleList = null;
        try {
            connection = BaseDao.getConnection();
            roleList=roleDao.getRoleList(connection);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return roleList;
    }

    @Test
    public void getRoleListTest(){
        RoleService roleService = new RoleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        for (Role role: roleList) {
            System.out.println(role.toString());
        }
    }
}
```

至此为了用户管理的三条数据库路线就搭建完成了，剩下的就是控制层Servlet的事了。

### 2. UserServlet处理请求

由于前端不是自己写的（也不会写），写这层属实折磨，只能先明确几个目标

前端为了展示数据，需要一些参数，包括

1. 用户列表userList，由于展示用户信息
2. 角色列表roleList，提供给选择框
3. 查询的用户名queryUserName和选择的用户职责queryUserRole，跳转后仍要将查询的要求显示出来，比较符合逻辑
4. 页总数totalPageCount、用户总数totalCount和当前页面currentPageNo，用于前端进行分页展示

Servlet的任务就是调用业务层获得数据，并将这些数据响应给前端。

```java
public class UserServlet extends HttpServlet {
    // Servlet复用，UserServlet不只修改密码，还要增删改查
    // 怎么在一个doGet中执行多个方法？
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if ("savepwd".equals(method)) {
            this.updatePwd(req, resp);
        } else if ("pwdmodify".equals(method)) {
            this.pwdModify(req, resp);
        }else if("query".equals(method)){
            this.query(req, resp);
        }
    }
    ...
    //  重难点
    public void query(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // 查询用户列表
        // 从前端获取数据,参数都是在前端约定好的名字
        String queryUserName = req.getParameter("queryname"); // 可能为空
        String roleTemp = req.getParameter("queryUserRole"); // 初始是0
        String pageIndex = req.getParameter("pageIndex"); // 当前页面
        int queryUserRole = 0; // 先设置0
        // 用业务对象进行业务操作
        UserServiceImpl userService = new UserServiceImpl();
        // 第一次进入这个请求，一定是第一页第一个开始，页面大小固定
        int currentPageNo = 1;
        int pageSize = 5;
        /* 可以不判断，因为即使为空 在Dao层就不会追加按名字查询的语句了
        if (queryUserName == null) {
            queryUserName = "";
        }*/
        if(roleTemp!=null && !roleTemp.equals("")){
            // roleTemp不是空了，把值转换为int
            queryUserRole = Integer.parseInt(roleTemp);
        }
        // 获取前端请求的页数
        if (pageIndex != null) {
            currentPageNo = Integer.valueOf(pageIndex);
        }
        // 查询出来的用户总数量
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        
        // 用页面工具类进行管理，也可以不用
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);  //当前页
        pages.setPageSize(pageSize);            //页面大小
        pages.setTotalCount(totalCount);        //总用户数量
        int totalPageCount = pages.getTotalPageCount();  //总页面数量

        //控制首页和尾页，前端也做了判断
        //如果页面小于第一页，就显示第一页
        if (currentPageNo < 1) {
            currentPageNo = 1;
            //如果当前页面大于最后一页，当前页等于最后一页即可
        } else if (currentPageNo > totalPageCount) {
            currentPageNo = totalPageCount;
        }

        // 获取用户列表，把数据返回给前端
        List<User> userList = null;
        userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);
        req.setAttribute("userList", userList);
        // 获取角色列表，把数据返回给前端
        List<Role> roleList = null;
        // 角色的业务，调用对应的业务对象
        RoleService roleService = new RoleServiceImpl();
        roleList = roleService.getRoleList();
        req.setAttribute("roleList", roleList);

        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("totalCount", totalCount);
        req.setAttribute("currentPageNo", currentPageNo);

        req.getRequestDispatcher("userlist.jsp").forward(req, resp);
    }
}
```

Servlet中分页的关键就是获取前端传过来的页数（pageIndex，虽然不知道怎么做的但也在url的参数中，如...&pageIndex=2），用页数去从数据库中取出对应顺序的数据返回给前端。

后端也进行页数判断的作用在于虽然前端不能直接输入非法的页数，但通过抓包修改参数（BurpSuit）仍可以将不安全的请求发到后端导致服务器崩溃，在后端加上验证可以最大程度避免安全问题。

### 3. 总结

SMBMS项目的一个重要功能就已经实现了，但因为不懂前端，写Servlet处理请求和响应数据的时候都很折磨。只能说后面找机会看看前端吧😵。

