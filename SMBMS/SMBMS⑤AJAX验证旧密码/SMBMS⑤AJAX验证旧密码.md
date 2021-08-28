## SMBMS⑤AJAX验证旧密码

### 1. 什么是AJAX？

**AJAX 是一种在无需重新加载整个网页的情况下，能够更新部分网页的技术。**

AJAX = Asynchronous Javascript And XML，即异步 JavaScript 和 XML，是一种用于创建快速动态网页的技术。

通过在后台与服务器进行少量数据交换，AJAX 可以使网页实现异步更新。这意味着可以在不重新加载整个网页的情况下，对网页的某部分进行更新。传统的网页（不使用 AJAX）如果需要更新内容，必需重载整个网页面。

### 2. AJAX的使用

#### 2.1 AJAX格式

接密码修改，在pwdmodify.js中使用了AJAX技术实现动态验证旧密码

```js
// 失去焦点
oldpassword.on("blur", function () {
        $.ajax({
           // 请求方式 GET
            type: "GET",
            // 提交的请求和参数
            // 此处相当于发起 path + "/jsp/user.do?method=pwdmodify&oldpassword=oldpassword.val()" 请求
            url: path + "/jsp/user.do",
            data: {method: "pwdmodify", oldpassword: oldpassword.val()}, //ajax传递的参数
            // 数据格式 json 主流开发都用json进行前后端交互
            dataType: "json",
            success: function (data) {
                if (data.result == "true") {//旧密码正确
                    validateTip(oldpassword.next(), {"color": "green"}, imgYes, true);
                } else if (data.result == "false") {//旧密码输入不正确
                    validateTip(oldpassword.next(), {"color": "red"}, imgNo + " 原密码输入不正确", false);
                } else if (data.result == "sessionerror") {//当前用户session过期，请重新登录
                    validateTip(oldpassword.next(), {"color": "red"}, imgNo + " 当前用户session过期，请重新登录", false);
                } else if (data.result == "error") {//旧密码输入为空
                    validateTip(oldpassword.next(), {"color": "red"}, imgNo + " 请输入旧密码", false);
                }
            },
            error: function (data) {
                //请求出错
                validateTip(oldpassword.next(), {"color": "red"}, imgNo + " 请求错误", false);
            }
        });
```

在oldpassword框失去焦点时调用AJAX方法，其中

1. type: "GET"为请求方式
2. url: path + "/jsp/user.do"为请求的路径
3. data: {method: "pwdmodify", oldpassword: oldpassword.val()}为请求携带的数据
4. dataType: "json"为数据格式
5. success: function (data)为请求成功，返回数据都在data中
6. error: function (data)为请求失败

**注意**：data中的数据格式不定，但主流开发都用json进行前后端交互；大概因为json是键值对，所以直接通过data.key的方式就可以获取到value了吧。

#### 2.2 Servlet获取数据

接密码修改，在UserServlet中实现了Servlet复用，多加一个pwdModify即可

```java
public class UserServlet extends HttpServlet {
    // Servlet复用，UserServlet不只修改密码，还要增删改查
    // 怎么在一个doGet中执行多个方法？
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("savepwd".equals(method)){
            this.updatePwd(req, resp);
        }else if("pwdmodify".equals(method)){
            this.pwdModify(req, resp);
        }
    }
    ...
	// 使用Session中的旧密码进行验证
    public void pwdModify(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // 从Session中获取用户
        Object object = req.getSession().getAttribute(Constants.USER_SESSION);
        // 获取前端传过来的oldpassword参数
        String oldpassword = req.getParameter("oldpassword");
        // 使用 Map 保存result的值
        Map<String, String> resultMap = new HashMap<String, String>();
        if(object == null){ // 取不到用户，Session失效了（设置Session失效时间）
            // 和前端约定好的返回值
            resultMap.put("result","sessionerror");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){ //旧密码为空 前端后端都应该验证
            resultMap.put("result","error");
        }else{
            // 获取旧密码
            String userPassword = ((User) object).getUserPassword();
            if( userPassword.equals(oldpassword)){ //旧密码输入正确
                resultMap.put("result","true");
            }else{ //旧密码输入错误
                resultMap.put("result","false");
            }
        }
        // 设置响应数据的格式
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        // JSONArray 阿里的JSON工具类，此处将Map转换为JSON
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush(); // ?
        writer.close();
    }
}
```

### 3. 总结

**流程**：前端通过AJAX发送请求，携带pwdmodify参数，告知Servlet使用pwdModify()方法并发送一个包含result的值的JSON数据给前端（使用 JSONArray将Map转换为 JSON），前端对result的值进行判断进行再对应的操作。

**注意**：Tomcat总是找不到Mavne导入的jar包，解决方式应有二

1. 直接从External Libraries中将Mavne项目导入的jar包复制一份到Tomcat下的lib文件夹中。
2. 在Project Structrue的Artifacts选项中，将jar包全部Put into /WEB-INF/lib。



初见AJAX结束，还是挺有用的，不过还要研究一下JSON捏🤔。