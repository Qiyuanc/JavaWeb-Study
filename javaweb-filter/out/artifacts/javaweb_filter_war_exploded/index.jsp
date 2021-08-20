<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/8/3
  Time: 14:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>$Title$</title>
  </head>
  <body>
  <%--要将ServletContext中的数据去出来，有多种方法--%>
  <h1>当前在线：<span><%=this.getServletConfig().getServletContext().getAttribute("OnlineCount")%></span></h1>
  <%--application 即 ServletContext--%>
  <h1>当前在线：<span><%=application.getAttribute("OnlineCount")%></span></h1>

  </body>
</html>
