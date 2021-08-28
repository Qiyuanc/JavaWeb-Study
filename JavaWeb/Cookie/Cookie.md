## Cookie/Session学习笔记

### 1. 会话

**会话**：会话是指一个终端用户与交互系统进行通讯的过程，比如从输入账户密码进入操作系统到退出操作系统就是一个会话过程。

**有状态会话**：客户端访问过服务器，下次访问时服务器就知道此客户端来过，就是有状态会话。

**一个网站怎么证明你来过？**

1. 服务器给客户端一个标志，客户端下次访问时带上标志，Cookie。
2. 服务器登记此客户端来过，Session。

### 2. 保存会话的两种技术

**Cookie**：客户端技术（请求，响应）。

**Session**：服务器技术，可以保存用户的会话信息。可以把信息或数据放在Session中。

常见场景：登录一个网站后，下次访问不再需要输入用户名密码。

### 3. Cookie

#### 3.1 Cookie是什么

Cookie是某些网站为了辨别用户身份，进行Session跟踪而储存在用户本地终端上的数据（通常经过加密），由用户客户端计算机暂时或永久保存的信息。

举例来说, 一个 Web 站点可能会为每一个访问者产生一个唯一的ID, 然后以 Cookie 文件的形式保存在每个用户的机器上。

#### 3.2 使用Cookie

1. 服务器从请求request中获取Cookie信息；
2. 服务器将Cookie信息响应response给客户端。

用到的Cookie方法

```java
//从客户端请求中获取Cookie
Cookie[] cookies = req.getCookies();
cookie.getName(); //获取cookie的Name属性
cookie.getValue(); //获取cookie的Value属性
//创建一个Cookie
Cookie timeCookie = new Cookie("lastLoginTime", System.currentTimeMillis() + "");
timeCookie.setMaxAge(24*60*60); //设置Cookie的有效期
resp.addCookie(timeCookie); //将Cookie信息响应给客户端
```

Demo1全部代码

Java中的Date和时区：https://blog.csdn.net/qq_28988969/article/details/103480911

```java
public class CookieDemo1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        //获取输出对象
        PrintWriter out = resp.getWriter();
        //从客户端请求中获取Cookie
        Cookie[] cookies = req.getCookies();
        //是否存在Cookie，两种情况
        if(cookies!=null){
            out.write("曾经的访问时间：");
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                //避免空指针异常
                if("lastLoginTime".equals(cookie.getName())){
                    //获取Cookie中的值并转换输出
                    //Long.parseLong(String)方法，将string参数解析为有符号十进制，返回一个long的result基本类型值
                    long lastLoginTime = Long.parseLong(cookie.getValue());
                    Date date = new Date(lastLoginTime);
                    out.write(String.valueOf(date));
                }
            }
        }else{
            out.write("第一次访问！");
        }
        //System.currentTimeMillis() 自公元时间以来的毫秒数，时间戳
        Cookie timeCookie = new Cookie("lastLoginTime", System.currentTimeMillis() + "");
        //设置Cookie的有效期 此处为一天
        timeCookie.setMaxAge(24*60*60);
        resp.addCookie(timeCookie);
    }
}
```

在浏览器检查中可以看到

![image-20210727185716089](F:\TyporaMD\JavaWeb\Cookie\image-20210727185716089.png)

#### 3.3 Cookie的细节问题

- 一个Cookie只能保存一个信息
- 一个Web站点可以给浏览器发送多个Cookie，有上限，50个？
- 浏览器中Cookie有上限，300个？
- Cookie一般会保存在本地的C:\Users\AppData下

**删除Cookie**

- 不设置有效期，关闭浏览器自动失效
- 设置有效时间为0，立即失效

```java
public class CookieDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //创建一个Cookie，名字要和被删除的一样
        Cookie timeCookie = new Cookie("lastLoginTime", System.currentTimeMillis() + "");
        //生命周期为0，立即失效
        timeCookie.setMaxAge(0);
        resp.addCookie(timeCookie);
    }
}
```

在访问/c1后访问/c2，/c1响应的cookie会被删除，此时再访问/c1相当于第一次访问。

#### 3.4 中文信息Cookie

发送和接收时最好使用URLEncoder和URLDecoder编码和解码。

```java
public class CookieDemo3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");

        Cookie[] cookies = req.getCookies();
        PrintWriter out = resp.getWriter();
        //遍历获取需要的Cookie
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            //避免空指针异常
            if("name".equals(cookie.getName())){
                //System.out.println(cookie.getValue());
                //out.write(cookie.getValue());
                //解码接收
                out.write(URLDecoder.decode(cookie.getValue(),"UTF-8"));
            }
        }
        //编码发送
        Cookie cncookie = new Cookie("name", URLEncoder.encode("祈鸢","UTF-8"));
        resp.addCookie(cncookie);
    }
}
```

虽然不用好像也行。

### 4. Session⭐

#### 4.1 Session是什么

Session 是一种客户端与Web应用之间一种安全高效的会话方式。一旦开启了 Session 会话，便可以在网站的任何页面使用保持这个会话，从而让客户端与Web应用之间建立了一种“会话”机制。

- 服务器会给每一个用户（浏览器）创建一个Session对象；
- 一个Session独占一个浏览器，只要浏览器没有关闭，这个Session就一直存在；
- 用户登录后，整个网站都可以访问这个Session，保存用户信息。

使用场景

- 保存登录用户的信息
- 保存购物车类的信息（物品加入购物车，保存在Session中，下单则提交数据库，不下单关闭浏览器则Session销毁，购物车信息也删除了）
- 网站中经常使用的数据可以保存在Session中

#### 4.2 使用Session

通过Session保存信息

```java
public class SessionDemo1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置编码
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        //获得Session
        HttpSession session = req.getSession();
        //向Session中存数据
        session.setAttribute("name",new Person("祈鸢",20));
        //获取Session的ID
        String id = session.getId();
        //判断Session是不是新创建的
        if(session.isNew()){
            out.write("Session创建成功,ID:"+session.getId());
        }else {
            out.write("Session已存在,ID:"+session.getId());
        }
        //Session创建时做了什么事情
        //Cookie jsessionid = new Cookie("JSESSIONID", id);
        //resp.addCookie(jsessionid);
    }
}
```

在另一个页面，通过Session获取信息

```java
public class SessionDemo2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        //获得Session
        HttpSession session = req.getSession();
        Person person = (Person) session.getAttribute("name");
        out.write(person.toString());
    }
}

```

删除信息、注销Session

```java
public class SessionDemo3 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        //删除Session中的信息
        session.removeAttribute("name");
        //手动注销Seesion，或者在web.xml中配置
        session.invalidate();
    }
}
```

```xml
  <!--设置Session-->
  <session-config>
    <!--有效时间，单位分钟-->
    <session-timeout>1</session-timeout>
  </session-config>
```

### 5. Session和Cookie的区别

- Cookie把用户的数据写给用户的浏览器，浏览器保存（可以保存多个）
- Session把用户的数据写到用户对应的Session中，服务器保存（保存重要信息，减少服务器资源的浪费）
- **当一个session第一次被启用时，一个独一的标识（SessionID？）被存储于本地的cookie中**

Tip: Session是针对每个用户（浏览器）的，ServletContext是属于Web服务器的，如要统计多少人访问了服务器，这个数据就要保存在ServletContext中。

**Q：为什么第一次访问一个网页，请求头中Cookie会包含SessionID？**

**A：还是没想明白。**

**https://blog.csdn.net/weixin_40208575/article/details/101868259**

**https://blog.csdn.net/i042416/article/details/87925869**

**这个细，以后再看https://www.cnblogs.com/xdp-gacl/p/3855702.html**

![image-20210727233629809](F:\TyporaMD\JavaWeb\Cookie\image-20210727233629809.png)

麻麻的，原来是进入localhost:8080/sc/的时候set的JSESSIONID，此时的cookie并不包含SESSIONID！上面的第一次并不是第一次，上当惹。

