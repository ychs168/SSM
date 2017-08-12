<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Spring4.3 + Spring MVC + MyBatis3.1 登录验证示例</title>
</head>
<body align=center>
<!-- 注意action的值：wuzhijun/login，这是个请求路径，对应着控制器类UserLoginController.java中两个@RequestMapping的配置 -->
   <form action="wuzhijun/login" method="post" >
         用户名<input name="username" type="text"><br><br>
         密 码    <input name="userpsw" type="text"><br><br>
        <input value="登录" type="submit">&nbsp;&nbsp;<input value="重填" type="reset"><br>
   </form>
</body>
</html>