## SMBMS⑥用户管理实现

### 1. 用户管理流程

![image-20210808200345064](F:\TyporaMD\SMBMS\SMBMS⑥用户管理实现\image-20210808200345064.png)

与修改密码直接跳转到pwdmodify.jsp接收数据不同，查询用户需要向Servlet发出query请求，查询到符合要求的用户后再返回到前端显示出来。

由于要显示用户基本信息和职责，同时还有根据职责查询的功能，需要进行连表查询。

####  1.1 导入页面

导入util.pageSupport类（其实是要自己写的），包含当前页码currentPageNo、总数totalCount、页面容量pageSize属性。

其中对属性的set设置了条件，限定了一些不安全的情况，如当前页码currentPageNo不小于0时才能set。在这里对其进行判断才是完整的封装，虽然放到业务层也可以做，但会导致代码看起来很乱。

导入用户管理页面包括userlist.jsp和rollpage.jsp，在只做查询用户的，添加用户啥的先不管了。

### 2. 获取用户数量

#### 2.1 UserDao接口

前端可以通过输入用户名查询，或者选择职责查询，或是两者结合查询，对应到SQL语句中，此处需要userName和userRole两个参数。

```java
public interface UserDao {
	...
    // 查询用户数量，参数为名字或职责，对应前端查询的两个选择
    public int getUserCount(Connection connection,String userName,int userRole) throws SQLException;
}
```

#### 2.2 UserDao接口实现类

```java
public class UserDaoImpl implements UserDao{
	...
    // 根据用户名或角色查询用户数量（难点）
    public int getUserCount(Connection connection, String userName, int userRole) throws SQLException {
        // 根据需求进行sql语句的拼接
        PreparedStatement pstm = null;
        ResultSet rs = null;
        int count = 0;

        if(connection!=null){
            // 这里使用StringBuffer方便拼接
            StringBuffer sql = new StringBuffer();
            sql.append("SELECT COUNT(*) as count from smbms_user u,smbms_role r where u.userRole=r.id ");
            // 用一个list来动态变化要传下去的param，对应不同的业务需求
            ArrayList<Object> list = new ArrayList<Object>();
            // 输入了用户名，就要追加上用户名查询
            if(!StringUtils.isNullOrEmpty(userName)){
                sql.append("and u.userName like ? ");
                // like模糊查询，要在字段前后加%，替换的时候会自动加上单引号变成 '%...%'
                list.add("%"+userName+"%");
            }
            if(userRole > 0){
                sql.append("and u.userRole = ? ");
                list.add(userRole);
            }
            // 再把list转换为数组
            Object[] params = list.toArray();
            System.out.println("UserDaoImpl---getUserCount:"+sql.toString()); // 方便看sql语句
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
            // 这里应该可判可不判，查不到count应该是0
            if(rs.next()){
                // 从结果集中获取数量，列名，且只有一行
                count = rs.getInt("count");
            }
        }
        BaseDao.closeResource(null,pstm,rs);
        return count;
    }
}
```

其中有几个难点和关键点

1. 因为业务涉及到仅用户名，仅职责，用户名加职责这种组合，所以使用了ArrayList进行参数动态地变化，最后再使用toArray转换回普通数组往下传。
2. 不同的需求对应不同的SQL语句，所以使用了StringBuffer进行SQL语句的拼接；判断参数是否有效，若有效则将该参数的语句拼接到基本语句中，同时将该参数加入ArrayList。
3. 使用了like进行模糊查询，要在字段前后加%，PreparedStatement在进行替换的时候会自动加上' '的。
4. 再回顾一下，rs是进行逐行读取，通过get+字段获取对应列的数据。

#### 2.3 UserService接口

```java
public interface UserService {
	...
    // 查询用户数量
    public int getUserCount(String userName,int userRole);
}
```

#### 2.4 UserService接口实现类

```java
public class UserServiceImpl implements UserService {
    // 业务层调用Dao层获取数据，需要引入Dao层
    private UserDao userDao;
    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }
    ...
    public int getUserCount(String userName, int userRole) {
        // 要关闭连接，要提升作用域
        Connection connection = null;
        // 设置返回值
        int count = 0;
        try {
            connection = BaseDao.getConnection();
            count = userDao.getUserCount(connection,userName,userRole);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            BaseDao.closeResource(connection,null,null);
        }
        return count;
    }

    @Test
    public void getUserCountTest(){
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount("祈", 1);
        //int userCount = userService.getUserCount(null, 0);
        System.out.println(userCount);
    }
}
```

业务层只要做最基本的调用Dao层往下传参数就行了，最好在业务层进行一下测试保证这个业务没有问题。

### 3. 获取用户列表

与上面大同小异，主要是返回值和SQL语句不同，这里就用写好的了。

#### 3.1 UserDao接口

```java
public interface UserDao {
	...
	// 查询用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception;
}
```

#### 3.2 UserDao接口实现类

```java
public class UserDaoImpl implements UserDao{
    ...
    // 获取查询的用户列表
	public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        // 存放查找出来的user对象并返回
        List<User> userList = new ArrayList<User>();
        if (connection != null) {
            StringBuffer sql = new StringBuffer();
            // 多表联合查询
            sql.append("select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id");
            List<Object> list = new ArrayList<Object>();
            if (!StringUtils.isNullOrEmpty(userName)) {
                sql.append(" and u.userName like ? ");
                list.add("%" + userName + "%");
            }
            if (userRole > 0) {
                sql.append(" and u.userRole = ? ");
                list.add(userRole);
            }
            // 在mysql数据库中，分页使用 limit startIndex，pageSize
            // 这里按创建日期creationDate降序排序了
            sql.append(" order by creationDate DESC limit ?,?");
            currentPageNo = (currentPageNo - 1) * pageSize;
            list.add(currentPageNo);
            list.add(pageSize);

            Object[] params = list.toArray();
            System.out.println("UserDaoImpl---getUserList:" + sql.toString());
            rs = BaseDao.execute(connection, pstm, rs, sql.toString(), params);
            while (rs.next()) {
                User _user = new User();
                _user.setId(rs.getInt("id"));
                _user.setUserCode(rs.getString("userCode"));
                _user.setUserName(rs.getString("userName"));
                _user.setGender(rs.getInt("gender"));
                _user.setBirthday(rs.getDate("birthday"));
                _user.setPhone(rs.getString("phone"));
                _user.setUserRole(rs.getInt("userRole"));
                _user.setUserRoleName(rs.getString("userRoleName"));
                userList.add(_user);
            }
            BaseDao.closeResource(null, pstm, rs);
        }
        return userList;
    }
}
```

在查询用户数量的基础上修改了SQL语句，改为查询用户信息，使用List数组存放从结果集rs中获取的一个个对象返回给上一层。

```mysql
select u.*,r.roleName as userRoleName from smbms_user u,smbms_role r where u.userRole = r.id
```

意为从smbms_user表和smbms_role表中查询符合u.userRole = r.id条件的 u.* 信息和r.roleName信息（？）。暂且理解为u.userRole --->  r.id ---> r.roleName。

#### 3.3 UserService接口

```java
public interface UserService {
	...
    // 查询用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);
}
```

#### 3.4 UserService接口实现类

```java
public class UserServiceImpl implements UserService {
    // 业务层调用Dao层获取数据，需要引入Dao层
    private UserDao userDao;
    public UserServiceImpl() {
        userDao = new UserDaoImpl();
    }
	...
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize) {
        // 要关闭连接，要提升作用域
        Connection connection = null;
        // 设置返回值
        List<User> userList = null;
        try {
            connection = BaseDao.getConnection();
            userList = userDao.getUserList(connection, queryUserName, queryUserRole, currentPageNo, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return userList;
    }

    @Test
    public void getUserListTest(){
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = userService.getUserList(null,0,1,5);
        for (User user:userList) {
            System.out.println(user.toString());
        }
    }
}
```

**注意**：参数中currentPageNo和pageSize表示当前页数，但在UserDaoImpl的getUserList方法中，有

```java
            currentPageNo = (currentPageNo - 1) * pageSize;
```

此处(currentPageNo - 1)中的currentPageNo是指当前页面，但左边的currentPageNo却是要填到

```sql
            sql.append(" order by creationDate DESC limit ?,?");
```

中的，是查询结果的索引值，此处就不该使用currentPageNo这个名字以免引起误会（起码我看晕了）。

**Tips**：limit子句用于限制查询结果返回的数量，如select * from tableName limit i,n，tableName为数据表，i为查询结果的索引值（默认从0开始），n为查询结果返回的数量。

### 4. 总结

写了这么多其实都是在干MVC中的Model该干的事，即执行业务，调用Dao层，查询数据库。再将数据给到Controller进行控制，哪里需要什么数据，该跳转到哪个页面等。再由View显示出来，就是完整的MVC结构了。

用户管理还没写完，下篇继续😶。
