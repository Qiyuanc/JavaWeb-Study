## Tomcat/Http复习笔记

Tomcat和Http知识快忘完了，复习一下。

### 1. Tomcat

#### 1. 1 下载解压Tocmat

懂得都懂，不再赘述。

#### 1.2 Tomcat文件夹结构

![image-20210719145114740](F:\TyporaMD\JavaWeb\Tomcat\image-20210719145114740.png)

#### 1.3 Tomcat的配置

在F:\Enviroment\apache-tomcat-9.0.39\conf下的server.xml中配置。

![image-20210719145348145](F:\TyporaMD\JavaWeb\Tomcat\image-20210719145348145.png)

可以修改启动的端口号，Tomcat默认端口8080

E：mysql默认端口3306

HTTPS默认端口443

HTTP默认端口80 ...

```xml
<Connector port="8080" protocol="HTTP/1.1"
           connectionTimeout="20000"
           redirectPort="8443" URIEncoding="UTF-8" />
```

可以修改Host地址

```xml
  <Host name="localhost"  appBase="webapps"
        unpackWARs="true" autoDeploy="true">
```

**Q：浏览器是怎么从域名访问到网站的？（计算机网络、DNS）**

**A：1. 查看本地Hosts文件中是否存在对应的域名映射，如localhost映射为127.0.0.1，本地主机；**

**若本地存在映射，则直接返回ip地址；**

**若不存在，则访问DNS服务器解析域名，解析成功返回对应ip地址。（怎么访问DNS服务器的？）**

#### 1.4 发布Web应用

将web项目放到服务器（Tomcat）指定的web应用文件夹下（webapps文件夹）下，此处为Helloworld

![image-20210719160643141](F:\TyporaMD\JavaWeb\Tomcat\image-20210719160643141.png)

网站结构

```java
-- Webapps
  - 网站应用文件夹
   - WEB-INF
    - web.xml
    - classes ：java程序
    - lib ：依赖库
   - index.html/jsp... 网站首页
   - static
    - css
    - js
    - img
   - ......
```

### 2. Http

#### 2.1 什么是Http

超文本传输协议（Hyper Text Transfer Protocol，HTTP）是一个简单的请求-响应协议，它通常运行在[TCP](https://baike.baidu.com/item/TCP/33012)之上。

- 文本：html、字符串...
- 超文本：图片、音乐、视频、地图...
- 默认端口：80

Https（Http security）是安全的Http协议。

- 默认端口：443

#### 2.2 Http发展

- Http/1.0：客户端和web服务器连接后，只能获得一个Web资源便断开连接
- Http/1.1：连接后可以获得多个Web资源，不需要多次连接

#### 2.3 Http请求

客户端——请求——服务器，以百度为例

##### 2.3.1 请求行

```java
Request URL: https://www.baidu.com/		//请求地址
Request Method: GET		//get方法/post方法
Status Code: 200 OK		//状态码：200
Remote Address: 14.215.177.39:443		//地址:端口
```

请求方式：**GET**、**POST**、HEAD、DELETE、PUT...

- GET：请求携带的参数较少，大小有限制，会在浏览器的URL地址里显示内容，不安全但高效。
- POST：请求携带的参数、大小都没有限制，不会在浏览器的URL地址里显示内容，安全但不高效。

##### 2.3.2 消息头

```java
Accept: text/html		//告诉浏览器支持的数据类型
Accept-Encoding: gzip, deflate, br		//支持的编码
Accept-Language: zh-CN,zh;q=0.9		//语言环境
Cache-Control: max-age=0		//缓存控制
Connection: keep-alive		//断开还是保持连接
```



#### 2.4 Http响应

服务器——响应——客户端，以百度为例

```java
Cache-Control: private		//缓存控制
Connection: keep-alive		//连接：保持连接 HTTP/1.1
Content-Encoding: gzip		//编码
Content-Type: text/html;charset=utf-8	//类型
```

##### 2.4.1 状态码

- 200：请求响应成功
- 3xx：请求重定向
- 4xx：找不到资源
- 5xx：服务器代码错误

**Q：向浏览器地址栏中输入地址并回车到展示出网页的过程中经历了什么？**