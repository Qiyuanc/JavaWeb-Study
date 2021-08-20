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
    <title>主页</title>
</head>
<body>

<%
    // 应该交给过滤器实现
/*    Object user_session = request.getSession().getAttribute("USER_SESSION");
    if(user_session == null){
        response.sendRedirect("/filter/login.jsp");
    }*/
%>
<h1>主页</h1>
<P><a href="/filter/logout">注销</a> </P>
</body>

</html>
