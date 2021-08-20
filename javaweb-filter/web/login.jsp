<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/8/3
  Time: 19:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%--获取服务器路径的方法！--%>
<h1>登录</h1>
<form action="${pageContext.request.contextPath}/login" method="post">
    <input type="text" name="username">
    <input type="submit">
</form>

</body>
</html>
