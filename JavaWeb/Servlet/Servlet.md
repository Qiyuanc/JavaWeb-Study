## Servlet学习笔记

### 1. Servlet简介

Servlet（Server Applet）是Java Servlet的简称，称为小服务程序或服务连接器，用Java编写的服务器端程序，具有独立于平台和协议的特性，主要功能在于交互式地浏览和生成数据，生成动态Web内容，狭义的Servlet是指Java语言实现的一个接口，广义的Servlet是指任何实现了这个Servlet接口的类。

开发一个Servlet程序需要两个步骤：

- 编写一个类实现Servlet接口
- 将编写好的Java类部署到Web服务器中

### 2. HelloServlet

1. 创建一个普通Maven项目，删除src文件夹，作为Maven父项目

2. Maven父子项目

   父项目的pom.xml中会有

   ```xml
   <modules>
       <module>servlet-01</module>
   </modules>
   ```

   子项目的pom.xml中会有

   ```xml
   <parent>
       <artifactId>javaweb-02-servlet</artifactId>
       <groupId>org.example</groupId>
       <version>1.0-SNAPSHOT</version>
   </parent>
   ```

   子项目可以直接使用父项目的jar包。

3. Maven环境优化

   3.1 修改web.xml为最新

   3.2 将Maven的结构补充完整

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
           xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                       http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
           version="4.0"
           metadata-complete="true">
   </web-app>
   ```

4. 编写一个Servlet

   4.1 创建一个普通类

   4.2 实现Servlet接口

   **继承关系：HttpServlet->GenericServlet->Servlet**

   **其中，Servlet和GenericServlet都只定义了service方法而没有实现，在HttpServlet中才实现，具体实现为根据不同的参数调用不同的方法，如参数为“GET”调用doGet，“POST”调用doPost等。**

   **所以，我们的类在继承HttpServlet后，要对其中的doGet等方法进行重写达到我们想要的效果。**

   **Tips：Ctrl+O => Select Methods to Override/Implement 快速重写方法**

   ​			**Alt+Enter => Introduce local variable 快速声明变量**

   ```java
   public class HelloServlet extends HttpServlet {
   
       // doGet 和 doPost 只是实现请求的不同方式，可以相互调用，业务逻辑都一样
       @Override
       protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           PrintWriter writer = resp.getWriter();// 响应流
           writer.print("Hello,Servlet");
   
       }
   
       @Override
       protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
           super.doPost(req, resp);
       }
   }
   ```

5. 编写Servlet的映射

   为什么需要映射：写出来的Java程序要通过浏览器访问，浏览器需要连接Web服务器，所以需要在Web服务器中注册Servlet，还要给它一个浏览器能访问的路径。

   ```xml
   <servlet>
       <!--注册Servlet-->
       <servlet-name>hello</servlet-name>
       <servlet-class>com.qiyuan.servlet.HelloServlet</servlet-class>
   </servlet>
   <servlet-mapping>
       <servlet-name>hello</servlet-name>
       <!--请求路径-->
       <url-pattern>/hello</url-pattern>
   </servlet-mapping>
   ```

6. 配置Tomcat

   端口号，JRE等

   <img src="F:\TyporaMD\JavaWeb\Servlet\image-20210720172952300.png" alt="image-20210720172952300" style="zoom: 50%;" />

   添加Artifact

   <img src="F:\TyporaMD\JavaWeb\Servlet\image-20210720173049045.png" alt="image-20210720173049045" style="zoom:50%;" />

   Applocation context即为访问的路径。

7. 启动服务器

   路径：localhost:8080/s1/hello

   启动成昆！

### 3.  Servlet原理

<img src="F:\TyporaMD\JavaWeb\Servlet\image-20210804214300115.png" alt="image-20210804214300115" style="zoom:67%;" />

### 4. Mapping映射

现在基本都是注解了，不过还要了解一下。

1. Servlet可以指定一个映射路径

   ```xml
   <servlet>
       <!--注册Servlet-->
       <servlet-name>hello</servlet-name>
       <servlet-class>com.qiyuan.servlet.HelloServlet</servlet-class>
   </servlet>
   <!--请求路径-->
   <servlet-mapping>
       <servlet-name>hello</servlet-name>
       <url-pattern>/hello</url-pattern>
   </servlet-mapping>
   ```

2. Servlet可以指定多个映射路径

   ```xml
   <!--请求路径-->
   <servlet-mapping>
       <servlet-name>hello</servlet-name>
       <url-pattern>/hello</url-pattern>
   </servlet-mapping>
   <servlet-mapping>
       <servlet-name>hello</servlet-name>
       <url-pattern>/hello2</url-pattern>
   </servlet-mapping>
   ```

   因为name对应上了，所以都指向一个。

3. Servlet可以指定通用映射路径，*号为通配符

   ```xml
   <servlet-mapping>
       <servlet-name>hello</servlet-name>
       <url-pattern>/hello/*</url-pattern>
   </servlet-mapping>
   ```

4. 可以自定义后缀实现请求，*前面不能加路径

   ```xml
   <servlet-mapping>
       <servlet-name>hello</servlet-name>
       <url-pattern>*.qiyuan</url-pattern>
   </servlet-mapping>
   ```

   以 .qiyuan 为后缀的请求都会跳转到映射为hello的servlet。 

5. 优先级

   指定了路径的映射比通配的映射优先级高，

   ```xml
   <servlet>
       <!--注册Servlet-->
       <servlet-name>error</servlet-name>
       <servlet-class>com.qiyuan.servlet.ErrorServlet</servlet-class>
   </servlet>
   <servlet-mapping>
       <servlet-name>error</servlet-name>
       <url-pattern>/*</url-pattern>
   </servlet-mapping>
   ```

   访问localhost:8080/s1/hello/suibianxie，仍会进入hello的Servlet。

### 5. ServletContext

web容器在启动的时候，会为每个web应用都创建一个对应的ServletContext对象，对象代表当前的web应用。

ServletContext的作用有：

#### 5.1 共享数据

在当前的Servlet中保存的数据可以在另一个Servlet中获得。

![image-20210804113814189](F:\TyporaMD\JavaWeb\Servlet\image-20210804113814189.png)


```java
//由这个Servlet来存数据
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        //this.getInitParameter();  获取初始化参数 返回String
        //this.getServletConfig();  获取Servlet配置 返回ServletConfig
        ServletContext context = this.getServletContext();//获取Servlet上下文 返回ServletContext
        String username = "祈鸢";
        context.setAttribute("username",username); // 将一个数据保存在Servlet中，数据名"username" 数据值"祈鸢"
        System.out.println("Hello");
    }
}
```

```java
//由这个Servlet来取数据
public class GetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServletContext context = this.getServletContext(); // 获取的ServletContext与HelloServlet中的是同一个
        String username = (String) context.getAttribute("username"); // 程序不知道该数据是什么类型 需要强转
        //设置编码
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("用户名：" + username);
        //流程： 进入Hello页面会存放一个数据，再进入Get页面会将这个数据打印出来
    }
}
```

```xml
<!--xml的配置！不要忘记！-->
<servlet>
    <servlet-name>hello</servlet-name>
    <servlet-class>com.qiyuan.servlet.HelloServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>hello</servlet-name>
    <url-pattern>/hello</url-pattern>
</servlet-mapping>

<servlet>
    <servlet-name>get</servlet-name>
    <servlet-class>com.qiyuan.servlet.GetServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>get</servlet-name>
    <url-pattern>/get</url-pattern>
</servlet-mapping>
```

#### 5.2 请求转发

与用request转发类似，使用相同的方法。

```java
public class DispatchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        //RequestDispatcher dispatcher = context.getRequestDispatcher("/getp"); 
        // 设置转发地址
        //dispatcher.forward(req,resp); // 将这里的参数转发出去
        context.getRequestDispatcher("/getp").forward(req,resp); // 一句话
    }
}
```

#### 5.3 获取初始化参数

```xml
<!--配置一些初始化参数-->
<context-param>
    <param-name>url</param-name>
    <param-value>jdbc:mysql://localhost:3306/mybatis</param-value>
</context-param>
```

```java
public class GetPServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        String url = context.getInitParameter("url"); //xml中对应的名字
        resp.getWriter().print(url);
    }
}
```

#### 5.4 读取资源文件

在java目录下或resource目录下创建properties资源文件，在运行后都会被打包到classes目录下，这个路径就是classpath，类路径。

通过文件流获取资源

```java
public class PropertiesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        InputStream in = context.getResourceAsStream("/WEB-INF/classes/db.properties"); // 以流形式获取资源文件
        Properties prop = new Properties();
        prop.load(in);
        String username = prop.getProperty("username");
        String password = prop.getProperty("password");
        //设置编码
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().print("用户名："+username+"\n"+"密码："+password);
    }
}
```

经典白学= =。