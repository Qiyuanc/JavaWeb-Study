## SMBMS③注销及权限过滤

完善登录模块，加入注销及权限过滤。

### 1. 注销功能

思路：移除Session，并返回登录页面。

```java
public class LogoutServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 移除Session
        req.getSession().removeAttribute(Constants.USER_SESSION);
        // 不加上项目路径，会走到localhost:8080/smbms/jsp/login.jsp
        resp.sendRedirect(req.getContextPath()+"/login.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

注册Servlet

```xml
    <servlet>
        <servlet-name>LogoutServlet</servlet-name>
        <servlet-class>com.qiyuan.servlet.user.LogoutServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>LogoutServlet</servlet-name>
        <url-pattern>/jsp/logout.do</url-pattern>
    </servlet-mapping>
```

这样就可以通过发起/logout.do的请求进行注销，但由于没有进行权限过滤，注销并移除Session后，直接输入/jsp/frame.jsp的链接也能进入主页。

### 2. 权限过滤

编写SysFilter过滤器对需要登录的/jsp下的请求进行过滤，若登录后访问则通过，否则跳转到error.jsp页面。

```java
public class SysFilter implements Filter {
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        // 从Session中获取登录信息
        User user = (User) request.getSession().getAttribute(Constants.USER_SESSION);
        // 已注销或未登陆
        if( user == null){
            // 加上项目路径
            response.sendRedirect("/smbms/error.jsp");
        }else { // 已登录 放行
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
    
    public void destroy() {
    }
}

```

注册过滤器，设置过滤的请求

```xml
    <!--用户登录过滤器-->
    <filter>
        <filter-name>SysFilter</filter-name>
        <filter-class>com.qiyuan.filter.SysFilter</filter-class>
    </filter>
    <filter-mapping>
        <filter-name>SysFilter</filter-name>
        <url-pattern>/jsp/*</url-pattern>
    </filter-mapping>
```

**注意**：此处error.jsp中若含有 ↓，则在访问时会报404错误，错误原因未知，也不想深究了。

```html
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
```

配置完成后若直接访问或注销完再发起/jsp下的请求，就会被过滤器直接重定向到error页面了。



至此SMBMS的登录模块就实现完成了（密码匹配也加上去了🙄）。