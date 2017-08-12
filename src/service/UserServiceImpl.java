/**
 * 
 */
package com.ychs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ychs.dao.IUserDao;
import com.ychs.vo.Users;

/**
 * 实现IUserService接口的类
 * 
 * @author wuzhijun
 * @version 1.0
 *
 */

@Service  //告知spring，该类要被纳入spring的管理范围
public class UserServiceImpl implements IUserService { 
	
	@Autowired  // 这个属性的赋值由spring自动完成
	private IUserDao userdao; 
	
	public Users login(String username, String userpsw) {
		return userdao.checkUser(username, userpsw);//调用userdao对象的方法，检查用户名和密码是否正确。
	}

}
