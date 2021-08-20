<%--
  Created by IntelliJ IDEA.
  User: ASUS
  Date: 2021/7/30
  Time: 23:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%--内置对象--%>
<%
    pageContext.setAttribute("name1","Qiyuan1"); //保存的数据在一个页面中有效
    request.setAttribute("name2","Qiyuan2"); //保存的数据在一次请求中有效，请求转发也会携带这个数据
    session.setAttribute("name3","Qiyuan3"); //保存的数据在一次会话中有效，从打开浏览器到关闭浏览器
    application.setAttribute("name4","Qiyuan4"); //保存的数据在服务器中有效，从打开服务器到关闭服务器
    pageContext.forward("/pageContext2.jsp");
%>
<%--通过pageContext来取--%>
<%
    // 从底层找到高层，类似JVM双亲委派机制
    // 作用域pageContext -> request -> session -> application
    String name1 = (String) pageContext.getAttribute("name1");
    String name2 = (String) pageContext.getAttribute("name2");
    String name3 = (String) pageContext.getAttribute("name3");
    String name4 = (String) pageContext.getAttribute("name4");
    String name5 = (String) pageContext.getAttribute("name5");
%>
<%--使用EL表达式输出--%>
<h1>取出的值为</h1>
<h3>${name1}</h3>
<h3>${name2}</h3>
<h3>${name3}</h3>
<h3>${name4}</h3>

</body>
</html>
