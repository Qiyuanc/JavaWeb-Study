<html>
<head>
    <title>login</title>
</head>
<body>
<!--提交表单-->
<%--提交的路径需要找到类路径--%>
<%--${pageContext.request.contextPath}代表当前项目--%>
<form action="${pageContext.request.contextPath}/login" method="get">
    用户名：<input type="text" name="username"> <br>
    密码：<input type="password" name="password"> <br>
    <input type="submit">
</form>
</body>
</html>
