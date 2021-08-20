<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/7/30
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>标题</title>
  </head>
  <body>
  <%--JSP表达式
  将程序的输出，输出到客户端
  <%= 变量或表达式%>
  --%>
  <%=new java.util.Date()%>
  <%--分割线--%>
  <hr>
  <%--JSP脚本片段
  可以<% %>中写Java代码
  --%>
  <%
    int sum = 0;
    for (int i = 0; i <= 100; i++) {
      sum += i;
    }
    out.println("<h1>Sum=" + sum + "</h1>");
  %>
  <%--分割线--%>
  <hr>
  <%--脚本片段再实现--%>
  <%
    int x = 10;
    out.println(x);
  %>
  <p>这是一个JSP文档</p>
  <%
    int y = 20;
    out.println(y);
  %>
  <%--分割线--%>
  <hr>
  <%--在代码中嵌入HTML元素--%>
  <%
    for (int i = 0; i < 5; i++) {
  %>
  <h2>HELLO,WORLD <%=i%></h2>
  <%
    }
  %>
  <hr>
  <%--定义到Service方法之外，使用<%! %>标签--%>
  <%!
    static {
      System.out.println("FQQY");
    }
    private int globalVar = 0;
    public void Qiyuan(){
      System.out.println("FQQY");
    }
  %>
  </body>
</html>
