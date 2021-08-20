package com.qiyuan.dao.role;

import com.qiyuan.entity.Role;

import java.sql.Connection;
import java.util.List;

/**
 * @ClassName RoleDao
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/9 16:05
 * @Version 1.0
 **/
public interface RoleDao {
    // 获取角色列表
     public List<Role> getRoleList(Connection connection) throws Exception;
}