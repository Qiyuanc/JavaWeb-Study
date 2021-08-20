<%@ page import="jakarta.servlet.http.HttpServletMapping" %><%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/7/30
  Time: 23:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    pageContext.forward("/index.jsp");// 等同于
    //request.getRequestDispatcher("/index.jsp").forward(request,response);
%>
</body>
</html>
