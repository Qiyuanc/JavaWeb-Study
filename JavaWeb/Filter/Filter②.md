## Filter笔记②

### 使用Filter实现权限拦截

#### 1.1 不使用Filter

登录页面login.jsp，通过pageContext可以获取服务器路径

```jsp
<%--获取服务器路径的方法！--%>
<h1>登录</h1>
<form action="${pageContext.request.contextPath}/login" method="post">
    <input type="text" name="username">
    <input type="submit">
</form>
```

LoginServlet，对应路径/filter/login

若用户名正确，登录成功则将USER_SESSION的值保存到Session中并重定向到登录成功页面success.jsp，否则重定向到error.jsp

```java
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        if("qiyuan".equals(username)){ // 登录成功
            req.getSession().setAttribute("USER_SESSION",req.getSession().getId());
            resp.sendRedirect("/filter/sys/success.jsp");
        }else{
            resp.sendRedirect("/filter/error.jsp");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

LogoutServlet，对应路径/filter/logout

登陆成功后可以选择注销，发起/filter/logout请求，若Session中存在USER_SESSION则将其移除，若不存存在直接重定向回到登录页面

```java
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object user_session = req.getSession().getAttribute("USER_SESSION");
        if(user_session != null){
            req.getSession().removeAttribute("USER_SESSION");
            resp.sendRedirect("/filter/login.jsp");
        }else{
            resp.sendRedirect("/filter/login.jsp");
        }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

登录成功后的页面success.jsp

先检查USER_SESSION是否存在，若不存在则转发到登录页面，存在则进入主页

```jsp
<%
    // 应该交给过滤器实现
    Object user_session = request.getSession().getAttribute("USER_SESSION");
    if(user_session == null){
        response.sendRedirect("/filter/login.jsp");
    }
%>
<h1>主页</h1>
<P><a href="/filter/logout">注销</a> </P>
</body>
```

#### 1.2 使用Filter

SysFilter，执行对/sys路径下所有请求的过滤

SysFilter做的事情同上，检查Session中是否存在USER_SESSION以确定是否有权限访问/sys下的页面

```java
public class SysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //ServletRequest是HttpServletRequest的父类 没有getSession方法 需要强转
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        Object user_session = req.getSession().getAttribute("USER_SESSION");
        if(user_session == null){
            resp.sendRedirect("/filter/error.jsp");
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }
    public void destroy() {
    }
}
```

```jsp
<!--/sys目录下存放需要权限的页面-->
<filter>
    <filter-name>sysfilter</filter-name>
    <filter-class>com.qiyuan.filter.SysFilter</filter-class>
</filter>
<filter-mapping>
    <filter-name>sysfilter</filter-name>
    <url-pattern>/sys/*</url-pattern>
</filter-mapping>
```

Tips："USER_SESSION"常量使用多次，可以提取常量到utils.Constant中

```java
public class Constant {
    public String USER_SESSION = "USER_SESSION";
}
```

引用时直接使用Constant.USER_SESSION，这样一来当需要改变常量名的时候直接修改Constant中的名称即可将项目中的常量都改变。

还有个VIP的应用，创建一个过滤器，获取User的VIP等级，重定向到/sys/VIP[ ]不同等级的页面。想是想明白了😶。