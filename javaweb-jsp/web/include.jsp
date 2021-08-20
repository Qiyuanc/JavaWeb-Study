<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/7/30
  Time: 18:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Title</title>
</head>
<body>
    <%--@include会将页面结合，将被包含页面的内容放到包含它的页面中去，可能会有重定义问题--%>
    <%@include file="common/header.jsp"%>
    <h2>主体</h2>
    <%@include file="common/footer.jsp"%>
    <hr>
    <%--JSP标签--%>
    <%--jsp:include通过拼接页面实现，本质是三个，命名空间不同--%>
    <jsp:include page="/common/header.jsp"/>
    <h2>主体</h2>
    <jsp:include page="/common/footer.jsp"/>
</body>
</html>
