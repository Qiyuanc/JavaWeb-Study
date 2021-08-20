<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/7/26
  Time: 18:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
