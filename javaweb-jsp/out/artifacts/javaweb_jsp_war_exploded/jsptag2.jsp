<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/7/31
  Time: 20:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>这里是JSPTAG2</h1>
<%--取参数，EL表达式获取不了request对象--%>
名字：<%=request.getParameter("name")%>
年龄：<%=request.getParameter("age")%>
</body>
</html>
