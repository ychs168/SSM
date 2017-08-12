/**
 * 
 */
package com.ychs.web;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ychs.service.IUserService;
import com.ychs.vo.Users;

/**
 * 接收用户登录验证请求的控制器类 UserLoginController，该类要和网页打交道，用于接收网页发来的请求；最后处理网页请求的是
 * 这个类中的某个方法；需要搞清楚的是：网页来的请求具体交给哪个方法处理（也就是一个请求过来时调用哪个方法）？因此这里很重要的是
 * 请求路径与具体方法映射的配置（请求映射），也就是@RequestMapping的配置，请仔细研究以下代码。
 * @author wuzhijun
 * @version 1.0 *
 */

@Controller  //告知spring，该类是控制器类，也就是要 把这类纳入spring的管理范围
@RequestMapping("/wuzhijun") // 这一行也可以去掉，如果这样写的话，那么在网页上访问该类的方法时需要带一个/wuzhijun的路径，也叫命名空间
public class UserLoginController {
	private static Logger logger = Logger.getLogger(UserLoginController.class);//记录日志
	
	@Autowired // 该属性的赋值由spring自动完成
	private IUserService userService; 
	
	@RequestMapping("/login")  //定义资源映射；网页上通过/login访问 login()方法，最终网页访问的路径是：<form action="wuzhijun/login">
	public String login(String username,String userpsw){		
		Users user = userService.login(username, userpsw);
		if(user != null) {
		   return "success"; // 浏览器显示 success.jsp,网页的保存位置在spring-web.xml中配置，在本例配置中网页存放在Eclipse工程的WebContent下
		} else {
			return "fail";  //浏览器显示fail.jsp，网页的保存位置在spring-web.xml中配置
		}
	}
}
