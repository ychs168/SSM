package test;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ychs.service.IUserService;
import com.ychs.vo.Users;

/**
 * 
 */

/**
 * 对登录验证的业务方法进行测试
 * @author wuzhijun
 * @version 1.0
 */
public class TestUserService extends BaseTest {
	@Autowired
    private IUserService userService;
	
	@Test
	public void testLogin(){  //测试登录的业务方法
		Users user = userService.login("wzj", "123");
		System.out.println("testLogin()的返回值是：  " + user);//此处使用直接输出，没有用log4j
	}	
}
