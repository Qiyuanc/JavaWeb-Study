package com.qiyuan.dao.user;

import com.qiyuan.entity.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    // 获取登录用户信息
    public User getLoginUser(Connection connection,String userCode) throws SQLException;
    // 修改当前用户密码
    public int updatePwd(Connection connection, int id, String pwd) throws SQLException;
    // 查询用户数量，参数为名字或职责，对应前端查询的两个选择
    public int getUserCount(Connection connection,String userName,int userRole) throws SQLException;
    // 查询用户列表
    public List<User> getUserList(Connection connection, String userName, int userRole, int currentPageNo, int pageSize) throws Exception;
    // 增加用户
    public int add(Connection connection, User user) throws Exception;
}
