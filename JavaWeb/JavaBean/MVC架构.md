## MVC架构笔记

### 1. 什么是MVC

MVC模式中，M（Model）是指业务模型，V（View）是指用户界面，C（Controller）则是控制器，使用MVC的目的是将M和V的实现代码分离，从而使同一个程序可以使用不同的表现形式。

### 2. 之前的架构

![image-20210803114540112](F:\TyporaMD\JavaWeb\JavaBean\image-20210803114540112.png)

用户直接访问控制层，控制层直接操作数据库。

```java
	Servlet -> CRUD -> 数据库
	问题：程序臃肿，不利于维护
	Servlet的代码中：处理请求、响应，视图跳转，处理JDBC，处理业务代码，处理逻辑代码
	
	架构：没有什么是加一层解决不了的！
	程序 -> JDBC -> Mysql、Oracle、SqlServer...
```

### 3. MVC三层架构

![image-20210803120548002](F:\TyporaMD\JavaWeb\JavaBean\image-20210803120548002.png)

模型Model

- 业务处理：业务逻辑（Service）
- 数据持久层：CRUD（Dao层）

视图View

- 展示数据
- 提供链接发起Servlet请求（a、form、img等）

控制器Controller

- 接收用户的请求：req请求参数，Session信息
- 交给业务层对应的代码处理
- 控制视图的跳转

```java
	登录 ---> 接收用户登录请求 ---> 处理用户请求（获取用户登录的参数） ---> 交给业务层处理登录业务	（判断用户名密码等是否正确） ---> Dao层查询登录信息是否正确 ---> 数据库
```

简单先了解一下观察🧐，后面再深入。