package com.qiyuan.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.qiyuan.entity.Role;
import com.qiyuan.entity.User;
import com.qiyuan.service.role.RoleService;
import com.qiyuan.service.role.RoleServiceImpl;
import com.qiyuan.service.user.UserService;
import com.qiyuan.service.user.UserServiceImpl;
import com.qiyuan.util.Constants;
import com.qiyuan.util.PageSupport;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName UserServlet
 * @Description TODO
 * @Author Qiyuan
 * @Date 2021/8/7 20:06
 * @Version 1.0
 **/
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
        }else if("add".equals(method)){
            this.add(req, resp);
        }else if("getrolelist".equals(method)){
            this.getRoleList(req, resp);
        }else if("ucexist".equals(method)){
            this.userCodeExist(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

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
        if (pageIndex != null) {
            // 获取前端请求的页数
            currentPageNo = Integer.valueOf(pageIndex);
        }

        // 查询出来的用户总数量
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        // 用页面工具类进行管理
        PageSupport pages = new PageSupport();
        pages.setCurrentPageNo(currentPageNo);  //当前页
        pages.setPageSize(pageSize);            //页面大小
        pages.setTotalCount(totalCount);        //总用户数量
        int totalPageCount = pages.getTotalPageCount();  //总页面数量

        //控制首页和尾页
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

    public void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 从Session中获取用户信息
        Object object = req.getSession().getAttribute(Constants.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;
        if (object != null && !StringUtils.isNullOrEmpty(newpassword)) {
            UserService userService = new UserServiceImpl();
            // 传递当前用户的ID 和前端的新密码 去修改 成功返回true
            flag = userService.updatePwd(((User) object).getId(), newpassword);
            if (flag) {
                req.setAttribute("message", "修改成功，请退出后再次登录");
                //密码修改完了 移除当前的Session 注销
                req.getSession().removeAttribute(Constants.USER_SESSION);
            } else {
                req.setAttribute("message", "修改失败");
            }
        } else {
            req.setAttribute("message", "新密码出错");
        }
        // 这里是什么情况？
        //req.getRequestDispatcher("pwdmodify.jsp").forward(req,resp);
        resp.sendRedirect(req.getContextPath() + "/jsp/pwdmodify.jsp");
    }

    // 使用Session中的旧密码进行验证
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 从Session中获取用户
        Object object = req.getSession().getAttribute(Constants.USER_SESSION);
        // 获取前端传过来的oldpassword参数
        String oldpassword = req.getParameter("oldpassword");
        // 使用 Map 保存result的值
        Map<String, String> resultMap = new HashMap<String, String>();
        if (object == null) { // 取不到用户，Session失效了（设置Session失效时间）
            // 和前端约定好的返回值
            resultMap.put("result", "sessionerror");
        } else if (StringUtils.isNullOrEmpty(oldpassword)) { //旧密码为空 前端后端都应该验证
            resultMap.put("result", "error");
        } else {
            // 获取旧密码
            String userPassword = ((User) object).getUserPassword();
            if (userPassword.equals(oldpassword)) { //旧密码输入正确
                resultMap.put("result", "true");
            } else { //旧密码输入错误
                resultMap.put("result", "false");
            }
        }
        // 设置响应数据的格式
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        // JSONArray 阿里的JSON工具类，此处将Map转换为JSON
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush(); // ?
        writer.close();
    }

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

        UserService userService = new UserServiceImpl();
        if (userService.add(user)) {
            response.sendRedirect(request.getContextPath() + "/jsp/user.do?method=query");
        } else {
            request.getRequestDispatcher("useradd.jsp").forward(request, response);
        }
    }

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
}
