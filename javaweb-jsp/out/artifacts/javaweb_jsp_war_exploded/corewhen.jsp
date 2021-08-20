<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/8/2
  Time: 16:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%--使用c:set存数据--%>
<c:set var="score" value="85"/>
<%--使用c:choose和c:when做判断--%>
<c:choose>
    <c:when test="${score>=90}">
        成绩优秀
    </c:when>
    <c:when test="${score>=80}">
        成绩良好
    </c:when>
    <c:when test="${score<80}">
        成绩不太行
    </c:when>
</c:choose>

</body>
</html>
