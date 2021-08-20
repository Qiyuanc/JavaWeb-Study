package com.qiyuan.service.user;

import com.qiyuan.entity.User;

import java.util.List;

public interface UserService {
    // 用户登录
    public User login(String username,String password);
    // 根据用户ID修改密码
    public boolean updatePwd(int id,String pwd);
    // 查询用户数量
    public int getUserCount(String userName,int userRole);
    // 查询用户列表
    public List<User> getUserList(String queryUserName, int queryUserRole, int currentPageNo, int pageSize);
    // 增加用户
    public boolean add(User user);
    // 根据UserCode查询用户是否存在
    public User selectUserCodeExist(String userCode);
}
