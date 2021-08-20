<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/8/2
  Time: 16:12
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--引入JSTL核心标签库--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h4>if测试</h4>
<hr>
<form action="coreif.jsp" method="get">
    <%--EL表达式获取表单中的数据
        ${param.参数名}  --%>
    <input type="text" name="username" value="${param.username}">
    <input type="submit" value="登录">
</form>
<%--java版
<%
    //如果是管理员则登录成功
    if(("qiyuan").equals(request.getParameter("username")))
        out.write("登陆成功");
%>
--%>
<%--JSTL版--%>
<c:if test="${param.username == 'qiyuan'}" var="isAdmin">
    <c:out value="管理员登陆"></c:out>
</c:if>
<c:out value="${isAdmin}"></c:out>

</body>
</html>
