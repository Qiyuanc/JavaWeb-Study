## Servlet学习笔记②

web服务器接收到客户端的http请求，针对这个请求会创建一个代表请求的HttpServletRequest对象和一个代表响应的HttpServletResponse对象。

- 如果要获取客户端发送的参数：HttpServletRequest
- 如果要向客户端发送信息：HttpServletResponse

### 1. HttpServletResponse

#### 1.1 分类

向浏览器发送数据的方法，在interface ServletResponse中

```java
    ServletOutputStream getOutputStream() throws IOException;
    PrintWriter getWriter() throws IOException;
```

向浏览器发送响应头的方法，在interface ServletResponse中

```java
    void setCharacterEncoding(String var1);
    void setContentLength(int var1);
    void setContentLengthLong(long var1);
    void setContentType(String var1);
    void setBufferSize(int var1);
```

一些状态码常量，在interface HttpServletResponse extends ServletResponse中

```java
    int SC_OK = 200; // 成功
    int SC_MULTIPLE_CHOICES = 300; // 重定向
    int SC_BAD_REQUEST = 400; // 请求错误
    int SC_INTERNAL_SERVER_ERROR = 500; // 服务器错误
```

对应Http响应的响应体和状态码。

#### 1.2 下载文件

1. 获取下载文件的路径
2. 获取下载的文件名
3. 设置浏览器支持下载(Content-Disposition)
4. 获取下载文件的输入流
5. 创建缓冲区
6. 获取OutptStream对象
7. 将FileOutputStream流写到Buffer缓冲区
8. 用OutptStream将缓冲区数据输出到浏览器

```java
public class FileServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1. 获取下载文件的路径 
        //String realPath = this.getServletContext().getRealPath("/1.jpg"); //不行
        String realPath = "F:\\IntelliJ IDEAProject\\javaweb\\javaweb-02-servlet\\response\\target\\classes\\艾拉.jpg";
        System.out.println("下载路径："+realPath);
        //2. 获取下载的文件名
        String filename = realPath.substring(realPath.lastIndexOf("\\") + 1);
        //3. 设置浏览器支持下载(Content-Disposition),使用URLEncoder转换中文编码，否则中文显示为 _
        resp.setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode(filename,"UTF-8"));
        //4. 获取下载文件的输入流
        FileInputStream in = new FileInputStream(realPath);
        //5. 创建缓冲区
        int len = 0;
        byte[] buffer = new byte[1024];
        //6. 获取OutptStream对象
        ServletOutputStream out = resp.getOutputStream();
        //7. 将FileOutputStream流写到Buffer缓冲区
        //8. 用OutptStream将缓冲区数据输出到浏览器
        while((len=in.read(buffer))>0){
            out.write(buffer,0,len);
        }
        // 关闭IO流
        in.close();
        out.close();
    }
}
```

#### 1.3 简单验证码

```java
public class ImageServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //浏览器定时刷新
        resp.setHeader("refresh", "5");
        //在内存中创建一个图片
        BufferedImage image = new BufferedImage(80, 20, BufferedImage.TYPE_INT_RGB);
        //获取图片
        Graphics2D g = (Graphics2D) image.getGraphics();
        //设置背景,填充整个图片
        g.setColor(Color.white);
        g.fillRect(0, 0, 80, 20);
        //向图片写入随机数
        g.setColor(Color.blue);
        g.setFont(new Font(null,Font.BOLD,20));
        g.drawString(CreatNum(),0,20);

        //让浏览器以图片方式打开请求
        resp.setContentType("image/jpeg");
        //取消浏览器的缓存
        resp.setDateHeader("exprise",-1);
        resp.setHeader("Cache-Control","no-cache");
        resp.setHeader("Pragma","no-cache");

        //将图片写到浏览器
        ImageIO.write(image,"jpeg",resp.getOutputStream());
    }
}
```

```java
//生成随机数
public String CreatNum() {
    Random random = new Random();
    String num = random.nextInt(9999) + "";
    StringBuffer sb = new StringBuffer();
    //不够四位的 补0
    for (int i = 0; i < 4-num.length(); i++) {
        sb.append("0");
    }
    num = sb.toString()+num;
    return num;
}
```

#### 1.4 实现重定向

一个Web资源B收到客户端A的请求后，通知客户端A去寻找另一个Web资源C，就是重定向。

```java
public class RedirectServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //resp.setHeader("Location","/r/image");
        //resp.setStatus(302);
        resp.sendRedirect("/r/image"); // 重定向
    }
}
```

浏览器检查可以发现有两个请求，其中redirect的状态码302即重定向的状态码

<img src="F:\TyporaMD\JavaWeb\Servlet\image-20210726150802816.png" alt="image-20210726150802816" style="zoom:80%;" />

#### 1.5 转发和重定向的区别⭐

相同点

- 页面都会改变

不同点

- **转发时，url地址不会发生变化，浏览器发送一次请求，是服务器内部行为，307**

  ```java
  // 使用request，内部行为不需要完整路径
  // 完整路径为http://localhost:8080/req/success.jsp
  req.getRequestDispatcher("/success.jsp").forward(req,resp)
  ```

- **重定向时，url地址会发生变化，浏览器发送两次请求，需要客户端再次请求，302**

  ```java
  // 使用response，浏览器再次请求需要完整路径
  // 完整路径为http://localhost:8080/r/image
  resp.sendRedirect("/r/image"); 
  ```

<img src="F:\TyporaMD\JavaWeb\Servlet\image-20210803212759719.png" style="zoom:80%;" />

### 2. HTTPServletRequest

HTTPServletRequest代表客户端的请求，用户通过Http协议访问服务器，Http请求中的信息会被封装到HTTPServletRequest对象中，服务器通过HTTPServletRequest对象的方法可以获得客户端发送的信息。

#### 2.1 登录后重定向到另一页面

```java
public class RequestServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入Request请求");
        //使用参数处理请求
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println(username+":"+password);
        //重定向页面->SUCCESS。jsp
        //重定向需要给浏览器完整路径
        resp.sendRedirect("/r/success.jsp");
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```
#### 2.2 登录后请求转发到到另一页面

```java
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码,避免控制台信息乱码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        //获取表单提交的信息
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String[] hobbies = req.getParameterValues("hobbies");
        //打印出来看看
        System.out.println(username);
        System.out.println(password);
        System.out.println(Arrays.toString(hobbies));
        //通过请求转发 							
        //转发是服务器内部跳转故不需要完整路径
        //req.getRequestDispatcher(req.getContextPath()+"/success.jsp").forward(req,resp);
        req.getRequestDispatcher("/success.jsp").forward(req,resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
```

jsp和xml的约束

```jsp
<%--index.jsp--%>
<html>
<head>
    <title>登录</title>
</head>
<body>
    <h1>登录</h1>
    <div style="text-align: center">
        <%--以post方式提交表单到login请求  --%>
        <form action="${pageContext.request.contextPath}/login" method="post">
            用户名:<input type="text" name="username"> <br>
            密码:<input type="password" name="password"> <br>
            喜好:
            <input type="checkbox" name="hobbies" value="1">1
            <input type="checkbox" name="hobbies" value="2">2
            <input type="checkbox" name="hobbies" value="3">3
            <input type="checkbox" name="hobbies" value="4">4
            <input type="submit">
        </form>
    </div>
</body>
</html>
```

```xml
<!--让/login去寻找Login的Servlet，约束在上面的form action中-->
<servlet>
    <servlet-name>login</servlet-name>
    <servlet-class>com.qiyuan.servlet.LoginServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>login</servlet-name>
    <url-pattern>/login</url-pattern>
</servlet-mapping>
```

