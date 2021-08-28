## JavaBean笔记

### 1. 什么是JavaBean

JavaBean 是一种JAVA语言写成的可重用组件。为写成JavaBean，类必须是具体的和公共的，并且具有无参数的构造器。

总而言之，JavaBean是一个实体类，并且：

- 属性必须私有化
- 必须有一个无参构造器
- 必须有属性对应的get/set方法

JavaBean一般用来与数据的数据进行映射（ORM）。

ORM：对象关系映射（Object Relational Mapping）

- 表 —> 类
- 字段 —> 属性
- 行记录 —> 对象

### 2. JSP使用JavaBean

Tips：IDEA Database项配置连接数据库 P22

```java
public class People {
    private int id;
    private String name;
    private int age;
    private String address;
    // People()
    // get/set...
}
```

```jsp
<%
    //普通方法
    People p = new People();
    p.setId(10);
    p.setName("祈鸢2");
    p.setAge(20);
    p.setAddress("NJNU");
%>
<%--有点后面的样子了--%>
<jsp:useBean id="people" class="com.qiyuan.entity.People" scope="page"/>
<jsp:setProperty name="people" property="id" value="1"/>
<jsp:setProperty name="people" property="name" value="祈鸢"/>
<jsp:setProperty name="people" property="age" value="20"/>
<jsp:setProperty name="people" property="address" value="NJNU"/>

ID：<jsp:getProperty name="people" property="id"/>
姓名：<jsp:getProperty name="people" property="name"/>
年龄：<jsp:getProperty name="people" property="age"/>
地址：<jsp:getProperty name="people" property="address"/>
```

具体还要看后面怎么用🧐。