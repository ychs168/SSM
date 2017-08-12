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
 * @author lenovo
 *
 */
@Controller
@RequestMapping("/wuzhijun")
public class UserLoginController {
	private static Logger logger = Logger.getLogger(UserLoginController.class);
	@Autowired
	private IUserService userService;
	
	@RequestMapping("/login")
	public String login(String username,String userpsw){
		
		Users user = userService.login(username, userpsw);
		if(user != null) {
		   return "success";
		} else {
			return "fail";
		}
	}
}
