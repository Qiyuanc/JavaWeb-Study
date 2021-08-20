<%@ page import="com.qiyuan.entity.People" %><%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/8/2
  Time: 18:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%
    People p = new People();
    p.setId(10);
    p.setName("祈鸢2");
    p.setAge(20);
    p.setAddress("NJNU");
%>

<jsp:useBean id="people" class="com.qiyuan.entity.People" scope="page"/>
<jsp:setProperty name="people" property="id" value="1"/>
<jsp:setProperty name="people" property="name" value="祈鸢"/>
<jsp:setProperty name="people" property="age" value="20"/>
<jsp:setProperty name="people" property="address" value="NJNU"/>

ID：<jsp:getProperty name="people" property="id"/>
姓名：<jsp:getProperty name="people" property="name"/>
年龄：<jsp:getProperty name="people" property="age"/>
地址：<jsp:getProperty name="people" property="address"/>

</body>
</html>
