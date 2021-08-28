package com.qiyuan.dao.role;

import com.qiyuan.dao.BaseDao;
import com.qiyuan.entity.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName RoleDaoImpl
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/9 16:09
 * @Version 1.0
 **/
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
