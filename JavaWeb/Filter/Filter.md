## Filter笔记

### 1. 什么是Filter

Filter是对客户端访问资源的过滤，符合条件放行，不符合条件不放行，并且可以对目 标资源访问前后进行逻辑处理。

![image-20210803141326228](F:\TyporaMD\JavaWeb\Filter\image-20210803141326228.png)

### 2. 创建Filter

1. 编写类实现Filter接口

   ```java
   //此处的Filter属于javax.servlet
   public class CharacterEncodingFilter implements Filter
   ```

2. 实现接口中的方法

   ```java
   public class CharacterEncodingFilter implements Filter {
       //初始化，Web服务器启动就初始化了
       public void init(FilterConfig filterConfig) throws ServletException {
           System.out.println("CharacterEncodingFilter初始化");
       }
       /*1.过滤器中的所有代码，在过滤特定请求（在web.xml中配置）的时候都会执行
         2.必须让过滤器放行请求 filterChain.doFilter(servletRequest,servletResponse);
       * */
       public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
           servletRequest.setCharacterEncoding("UTF-8");
           servletResponse.setCharacterEncoding("UTF-8");
           servletResponse.setContentType("text/html;charset=UTF-8");
           //filterChain 过滤器链
           System.out.println("过滤前");
           //通过 doFilter 放行请求，若没有 doFilter 程序就在此被拦截
           filterChain.doFilter(servletRequest,servletResponse);
           System.out.println("过滤后");
       }
       //销毁，Web服务器关闭时销毁
       public void destroy() {
           System.out.println("CharacterEncodingFilter销毁");
       }
   }
   ```

3. 在web.xml中配置filter要过滤的请求

   ```xml
   <filter>
       <filter-name>encoding</filter-name>
       <filter-class>com.qiyuan.filter.CharacterEncodingFilter</filter-class>
   </filter>
   <filter-mapping>
       <filter-name>encoding</filter-name>
       <!--/servlet 下的请求都会经过这个过滤器-->
       <url-pattern>/servlet/*</url-pattern>
   </filter-mapping>
   ```

4. 测试用的Servlet

   ```java
   public class ShowServlet extends HttpServlet {
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           //resp.setCharacterEncoding("UTF-8");
           //交给过滤器实现编码
           resp.getWriter().write("你好，世界");
       }
       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           doGet(req, resp);
       }
   }
   ```

   对应的xml配置

   ```xml
   <servlet>
       <servlet-name>show</servlet-name>
       <servlet-class>com.qiyuan.servlet.ShowServlet</servlet-class>
   </servlet>
   <servlet-mapping>
       <servlet-name>show</servlet-name>
       <url-pattern>/servlet/show</url-pattern>
   </servlet-mapping>
   <servlet-mapping>
       <servlet-name>show</servlet-name>
       <url-pattern>/show</url-pattern>
   </servlet-mapping>
   ```

该Servlet可通过/show和/servlet/show访问，若通过/show访问，则不会经过过滤器，会出现乱码；若通过/servlet/show访问，则会经过过滤器，不会出现乱码。

### 3. 监听器（+）

监听器即当某一事件发生时，对其作出反应的构件。

利用监听器统计网站在线人数

1. 编写监听器类

   ```java
   //统计网站在线人数：统计Session，将数据保存在ServletContext中
   public class OnlineCountListener implements HttpSessionListener {
       //服务器启动时会产生Session，需要Redeploy重新部署才能正常
       //Session创建监听，Session创建就会触发
       public void sessionCreated(HttpSessionEvent httpSessionEvent) {
           System.out.println(httpSessionEvent.getSession().getId());
           ServletContext context = httpSessionEvent.getSession().getServletContext();
           Integer onlineCount = (Integer) context.getAttribute("OnlineCount");
           if (onlineCount == null) {
               onlineCount = new Integer(1);
           } else {
               int count = onlineCount.intValue();
               onlineCount = new Integer(count + 1);
           }
           context.setAttribute("OnlineCount", onlineCount);
       }
       //Session销毁监听，Session销毁就会触发
       public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
           System.out.println("销毁");
           ServletContext context = httpSessionEvent.getSession().getServletContext();
           Integer onlineCount = (Integer) context.getAttribute("OnlineCount");
           int count = onlineCount.intValue();
           onlineCount = new Integer(count - 1);
           context.setAttribute("OnlineCount", onlineCount);
       }
       /*Session销毁方式
       1.手动销毁 httpSessionEvent.getSession().invalidate();
       2.自动销毁 在web.xml中配置session-config，设置session存活时间
       * */
   }
   ```

2. 在web.xml中配置监听器

   ```xml
   <!--注册监听器，就一句话-->
   <listener>
       <listener-class>com.qiyuan.listener.OnlineCountListener</listener-class>
   </listener>
   ```

监听器了解怎么用即可，过滤器比较关键🤔。

