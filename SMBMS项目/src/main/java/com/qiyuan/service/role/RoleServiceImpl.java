package com.qiyuan.service.role;

import com.qiyuan.dao.BaseDao;
import com.qiyuan.dao.role.RoleDao;
import com.qiyuan.dao.role.RoleDaoImpl;
import com.qiyuan.entity.Role;
import com.qiyuan.entity.User;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.util.List;

/**
 * @ClassName RoleServiceImpl
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/9 16:19
 * @Version 1.0
 **/
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
