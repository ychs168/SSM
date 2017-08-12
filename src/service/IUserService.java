/**
 * 
 */
package com.ychs.service;

import com.ychs.vo.Users;

/**
 * 进行登录验证的业务接口IUserService
 * @author wuzhijun
 * @version 1.0
 *
 */
public interface IUserService {
	/**
	 * 登录验证业务方法
	 * @param username  用户名
	 * @param userpsw   密码
	 * @return  如果登录失败返回null，否则返回非空
	 */
	public Users login(String username,String userpsw);
}
