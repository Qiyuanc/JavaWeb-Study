package com.qiyuan.dao.user;

import com.mysql.cj.util.StringUtils;
import com.qiyuan.dao.BaseDao;
import com.qiyuan.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserDaoImpl
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/5 22:05
 * @Version 1.0
 **/
public class UserDaoImpl implements UserDao{
    public User getLoginUser(Connection connection, String userCode) throws SQLException {
        PreparedStatement pstm = null;
        ResultSet rs = null;
        User user = null;
        if (null != connection) {
            String sql = "select * from smbms_user where userCode=?";
            Object[] params = {userCode};
            rs = BaseDao.execute(connection, pstm, rs, sql, params);
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUserCode(rs.getString("userCode"));
                user.setUserName(rs.getString("userName"));
                user.setUserPassword(rs.getString("userPassword"));
                user.setGender(rs.getInt("gender"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhone(rs.getString("phone"));
                user.setAddress(rs.getString("address"));
                user.setUserRole(rs.getInt("userRole"));
                user.setCreatedBy(rs.getInt("createdBy"));
                user.setCreationDate(rs.getTimestamp("creationDate"));
                user.setModifyBy(rs.getInt("modifyBy"));
                user.setModifyDate(rs.getTimestamp("modifyDate"));
            }
        }
        BaseDao.closeResource(null, pstm, rs);
        return user;
    }

    public int updatePwd(Connection connection, int id, String pwd) throws SQLException {
        // 返回值作用域在外面
        int execute = 0;
        // 先判断连接是否为空，防止上层传入null导致异常
        if (connection!=null){
            // 准备参数
            PreparedStatement pstm = null;
            String sql = "update smbms_user set userPassword=? where id=?";
            Object[] param = {pwd,id};
            // 调用BaseDao中的更新方法
            execute = BaseDao.execute(connection, pstm, sql, param);
        }
        // 若返回值不为空，说明有行受到影响，即成功
        return execute;
    }

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
