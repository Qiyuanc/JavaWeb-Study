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
<h1>这里是JSPTAG1</h1>
<%--相当于localhost:8080/jsptag.jsp?name=qiyuan&age=20--%>
<jsp:forward page="/jsptag2.jsp">
    <jsp:param name="name" value="qiyuan"/>
    <jsp:param name="age" value="20"/>
</jsp:forward>

</body>
</html>
