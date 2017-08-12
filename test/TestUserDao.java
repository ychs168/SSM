package test;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ychs.dao.IUserDao;
import com.ychs.vo.Users;
/**
 * 对UserDao中的方法进行单元测试
 * @author wuzhijun
 * @version 1.0 
 *
 */
public class TestUserDao extends BaseTest {
	private static Logger logger = Logger.getLogger(TestUserDao.class);
	@Autowired
	private IUserDao userDao;

	@Test
	public void testCheckUser() throws Exception {	 
		Users user = userDao.checkUser("wzj", "123");//测试dao中的方法
		logger.info("testCheckUser()的执行结果是：  " + user); 
	}	 
}
