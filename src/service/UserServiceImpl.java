/**
 * 
 */
package com.ychs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ychs.dao.IUserDao;
import com.ychs.vo.Users;

/**
 * @author lenovo
 *
 */
@Service
public class UserServiceImpl implements IUserService { 
	@Autowired
	private IUserDao userdao;
	
	public Users login(String username, String userpsw) {
		return userdao.checkUser(username, userpsw);
	}

}
