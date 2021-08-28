package com.qiyuan.service.user;

import com.qiyuan.dao.BaseDao;
import com.qiyuan.dao.user.UserDao;
import com.qiyuan.dao.user.UserDaoImpl;
import com.qiyuan.entity.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/5 23:35
 * @Version 1.0
 **/

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

    public User login(String userCode, String userPassword) {
        Connection connection = null;
        User user = null;
        try {
            // 通过业务层调用对应的具体数据库操作
            connection = BaseDao.getConnection();
            // 获取登录用户的信息
            user = userDao.getLoginUser(connection, userCode);
            //System.out.println(user.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        //匹配密码
        if (null != user) {
            if (!user.getUserPassword().equals(userPassword)) {
                user = null;
            }
        }
        return user;
    }

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

    public User selectUserCodeExist(String userCode) {
        Connection connection = null;
        User user = null;
        try {
            connection = BaseDao.getConnection();
            user = userDao.getLoginUser(connection, userCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            BaseDao.closeResource(connection, null, null);
        }
        return user;
    }

    @Test
    public void getUserCountTest(){
        UserServiceImpl userService = new UserServiceImpl();
        int userCount = userService.getUserCount("祈", 1);
        //int userCount = userService.getUserCount(null, 0);
        System.out.println(userCount);
    }

    @Test
    public void getUserListTest(){
        UserServiceImpl userService = new UserServiceImpl();
        List<User> userList = userService.getUserList(null,0,1,5);
        for (User user:userList) {
            System.out.println(user.toString());
        }
    }

    @Test
    public void addTest(){
        UserServiceImpl userService = new UserServiceImpl();
        User user = new User();
        user.setUserCode("qiyuanccc");
        user.setUserName("qiyuan");
        user.setUserPassword("1234567");
        user.setGender(1);
        // 不知道该怎么传了 随便写的格式
        user.setBirthday(new Date(2000-01-01));
        user.setPhone("13111111111");
        user.setAddress("就这");
        user.setUserRole(1);
        System.out.println(userService.add(user));
    }
}
