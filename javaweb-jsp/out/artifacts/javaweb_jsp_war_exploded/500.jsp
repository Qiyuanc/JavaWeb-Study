<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/7/30
  Time: 16:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <%-- ${pageContext.request.contextPath} = /jsp --%>
    <IMG src="img/500.png" alt="500">
    <img src="./img/500.png">
    <img src="${pageContext.request.contextPath}/img/500.png">
</body>
</html>
