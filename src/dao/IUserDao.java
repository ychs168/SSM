/**
 * 
 */
package com.ychs.dao;

import org.apache.ibatis.annotations.Param;

import com.ychs.vo.Users;

/**
 * @author lenovo
 *
 */
 
public interface IUserDao {
	public Users checkUser(@Param("username") String username,@Param("userpsw") String userpsw);
}
