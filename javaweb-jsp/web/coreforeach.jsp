<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/8/2
  Time: 16:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%
    ArrayList<String> people = new ArrayList<>();
    people.add("qiyuan1");
    people.add("qiyuan2");
    people.add("qiyuan3");
    people.add("qiyuan4");
    people.add("qiyuan5");
    request.setAttribute("list",people);
%>
<%--var:遍历得到的对象，items:要遍历的对象容器--%>
<c:forEach var="people" items="${list}">
    <c:out value="${people}"/> <br/>
</c:forEach>
<%--begin: i=? end: i<=? step: i+=?--%>
<c:forEach var="people" items="${list}" begin="1" end="3" step="2">
    <c:out value="${people}"/> <br/>
</c:forEach>

</body>
</html>
