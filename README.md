# Spring MVC + Spring4.3+MyBatis3.1整合示例--登录验证
> 本示例是写给初学者入门的，省略了商业软件开发中的其它内容，仅展示三个框架整合的方法之一。热心的朋友可以加入项目，提供更多整合的方法及案例，为初学者提供一点帮助。
> [GitHub完整源码](https://github.com/ychs168/SSM)
> [完整工程下载](http://pan.baidu.com/s/1bpF7DKV)
#  最终目标：实现用户登录
   在网页上输入用户名和密码，点击登录按钮，系统提示登录失败或成功。
### 一、开发环境
 - Spring 4.3 
 - MyBatis 3.1
 
 - JUnit4.12(本例恰好用到了，与整合没必然关系)
 - Log4J 1.2(本例恰好用到了，与整合没必然关系)
 - C3P0 0.9 (本例恰好用到了，与整合没必然关系)
 
 - JDK8  
 - Tomcat8.0  
 - MySQL5
 
 - Eclipse Java EE IDE for Web Developers
 
>  注意：三个框架整合时候，JUnit、Log4J和C3P0不是为了三个框架整合才用的，没有它们也能整合。。这里使用他们是为了方便查看日志和进行单元测试。作为职业程序员，日志和单元测试是必须要会的。C3P0是个数据库连接池，正好我的项目用到了，与整合没有必然关系。 你的工作是要三个框架整合在一起工作！另外本例中用到了Java的注解（Annotation），不知道的自己百度或谷歌。

---

### 二、整合的基本思想
- 首先要知道：整合实际上很简单，初学者感到麻烦的原因是你还没有理解框架工作的真正原理，当然你需要亲自动手写，再体会理解，才能逐步明白框架的真谛。
- 其次：整个表现在3个地方
   1. jar包
   2. XML文件
   3. Java代码（注意代码中的注解Annotation）
- 再次：你要仔细分析一下这三个框架都是干什么的，用他们的目的是什么，一个一个将它们组合起来。
   1. MyBatis是干吗的？它有什么配置 
   2. Spring是干吗的？有哪些配置？ 
   3. Spring MVC是干吗的？它有什么配置 ？
   
  在本例的整合中，用到了XML配置，也同时用到了注解。

### 三、在Eclipse中建立Web工程
1.  复制jar包。jar包全部复制到工程的/WEB-INF/lib下，jar包的构成有：Spring相关的包、MyBatis相关的包、MySQL驱动程序包、JSTL包、JUnit包、Log4J包、C3P0包等。
[Jar包网盘地址](http://pan.baidu.com/s/1bplLvrD)
   >    这里使用直接复制的方式，为了降低初学者的难度，实际开发中使用Maven或Gradle。
2. 在src下建立包，一共7个包
-    com.ychs.dao 数据库访问代码放这个包里
-    com.ychs.service  业务逻辑处理代码放这里
-    com.ychs.vo  放那些简单的Java类（POJO）
-    com.ychs.web 放与浏览器端交互的控制器类代码
-    mapper  放MyBatis映射文件
-    spring  放与Spring有关的配置文件
-    test    放测试代码
3. 在src下建立两个文件
- jdbc.properties 这个文件自己写一下，很重要，与整合有直接关系。 用户存放数据库连接信息
- log4j.properties  服务于Log4J记录日志的文件，直接拷贝示例中的就行，与整合没有直接关系。



> 至此，一共建立了7个包，2个文件，目前这些包里是空的，备用。目前有内容的就是jdbc.properties和log4j.properties了。jdbc.properties文件自己写，log4j的复制示例中的即可。jdbc.properties内容如下：
```
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8
jdbc.username=root
jdbc.password=root
```
### 四、在MySQL里建表users,字段和类型如下：

```
  userid   varchar;
 username  varchar;
 userpsw   varchar;
```
建立表后，在数据库里添加一条测试数据，我这类录入的是：

```
 userid  username    userpsw
  100      wzj         123
```
> ### 开发步骤
>   dao层代码---> service代码   ----> 控制层代码 --->网页代码
  
### 五、开始写数据库访问Dao层的Java代码和配置
> 不同的代码放在不同的包里。写的时候从数据库访问这边开始，也就是从dao开始。dao这边使用MyBatis，需要写一个接口和一个mapper映射XML文件，他们放在不同的包里。

1. 准备写dao代。这个dao干什么？当然是：查询用户名和密码是否合法。这时候问题来了：我们用的是MyBaits，它里面有三个东西支持才能工作：Java接口、mapper映射文件和数据库连接配置文件！ 那肯定得赶紧做数据库连接配置文件，没有这个，一切戏就没法唱了！！那这个文件放哪里？内容是啥？？？
好了：我们在写dao的Java代码前，先要把数据库配置做完。这个配置文件我这里叫做 spring-dao.xml,我把他放在前面建立的spring包里面，这个文件里的配置与MyBatis整合有密切关系。
spring-dao.xml的内容如下：

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context.xsd">
	
	<!-- 配置spring与mybatis整合 -->
	
	<!-- 1.指定数据连接信息，在jdbc.properties文件中 -->
	<context:property-placeholder location="classpath:jdbc.properties" />

	<!-- 2.数据库连接池 C3P0配置-->
	<bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
		<!-- 配置连接池属性 -->
		<property name="driverClass" value="${jdbc.driver}" />
		<property name="jdbcUrl" value="${jdbc.url}" />
		<property name="user" value="${jdbc.username}" />
		<property name="password" value="${jdbc.password}" />

		<!-- c3p0连接池的私有属性 -->
		<property name="maxPoolSize" value="30" />
		<property name="minPoolSize" value="10" />
		<!-- 关闭连接后不自动commit -->
		<property name="autoCommitOnClose" value="false" />
		<!-- 获取连接超时时间 -->
		<property name="checkoutTimeout" value="10000" />
		<!-- 当获取连接失败重试次数 -->
		<property name="acquireRetryAttempts" value="2" />
	</bean>

	<!-- 3.配置SqlSessionFactory对象 -->
	<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<!-- 注入数据库连接池 -->
		<property name="dataSource" ref="dataSource" />
		 
		<!-- 扫描vo包 使用别名 -->
		<property name="typeAliasesPackage" value="com.ychs.vo" />
		
		<!-- 扫描sql配置文件:mapper需要的xml文件 ，本例中与MyBatis有关的mapper映射xml文件放在mapper包里-->
		<property name="mapperLocations" value="classpath:mapper/*.xml" />
	</bean>

	<!-- 4.配置扫描Dao接口包，有Spring动态实现Dao接口，注入到spring容器中 -->
	<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
		<!-- 注入sqlSessionFactory -->
		<property name="sqlSessionFactoryBeanName" value="sqlSessionFactory" />
		<!-- 给出需要扫描Dao接口包 -->
		<property name="basePackage" value="com.ychs.dao" />
	</bean>
</beans>
```
2. 正式写dao代码，在com.ychs.dao包里新建接口IUserDao.java
3. 写mapper映射文件，在mapper中新建UserMapper.xml文件

IUserDao.java代码：

```
public interface IUserDao {

	public Users checkUser(@Param("username") String username, @Param("userpsw") String userpsw);// 形参处出现的@Param是MyBatis提供的注解，@Param("username")中的username在UserMapper.xml文件里會用到
}

```
UserMapper.xml内容：

```
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
    PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
    "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.ychs.dao.IUserDao">
	<!-- 目的：为dao接口方法提供sql语句配置,#后的username和userpsw来自于IUserDao中的checkUser方法里面的@Param("username") -->
	<select id="checkUser" resultType="com.ychs.vo.Users">
		 SELECT * FROM users WHERE username=#{username} AND userpsw=#{userpsw}
	</select>	 
</mapper>
```
### ==dao层编写结束==。IUserDao.java和UserMapper.xml配置写完了，可以测试了，现在开始测试，看看写的数据库访问代码能用不，不要等到全写完再测试，商业软件开发不是这个路子。要及时进行单元测试。测试代码放在工程的test包里，有两个文件分别是：BaseTest.java和TestUserDao.java，代码如下：
BaseTest.java

```
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
 
/**
 * 服务于JUnit测试，通过注解指定要加载的xml文件
 * @author wuzhijun
 * @version 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml", "classpath:spring/spring-service.xml" })
public class BaseTest {

}
```
TestUserDao.java

```
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
		logger.info("testCheckUser()执行结果是：" + user); 
	}	 
}

```
执行TestUserDao.java,看看日志中的输出，我这里是：
12:39:23,139  INFO - testCheckUser()的执行结果是：  com.ychs.vo.Users@b86de0d  说明查到对象了，也就是登录成功。把用户名或密码在代码里写错了，在执行看看输出？？？
### 六、开始写service层代码和配置
1. 在com.ychs.service包里建立service接口和实现类，注意理解这些文件里的注解Annotation
2. 在spring包里建立spring-service.xml文件，配置service

IUserService.java代码：

```
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

```
UserServiceImpl.java代码：

```
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

```
spring-service.xml内容：

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
	xmlns:context="http://www.springframework.org/schema/context"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context.xsd
	http://www.springframework.org/schema/tx
	http://www.springframework.org/schema/tx/spring-tx.xsd">
	
	<!-- 扫描service包下所有使用注解的类 ，这些类会被纳入spring的管理范围，这里的包是放业务service类的包-->
	<context:component-scan base-package="com.ychs.service" />

	<!-- 配置事务管理器 -->
	<bean id="transactionManager"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<!-- 注入数据库连接池 -->
		<property name="dataSource" ref="dataSource" />
	</bean>

	<!-- 配置基于注解的声明式事务 -->
	<tx:annotation-driven transaction-manager="transactionManager" />
</beans>
```
### ==此时Service层写完了。== 现在可以对我们写的业务层代码做测试了。需要注意，在开发推进中，xml文件的个数在增加，目前spring包下的xml文件已经有2个了。建立测文件类TestUserService.java，代码如下：

```
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

```
测试输出结果是：testLogin()的返回值是：  com.ychs.vo.Users@b86de0d；说明能查到数据，登录成功了；把用户名和密码改一下再试试；
> 经过测试，service和dao层都能正常工作，我们离目标越来越近啦！继续！
### 七、写控制层代码和配置
1. 在com.ychs.web中建立UserLoginController.java文件
2. 在spring包中建立spring-web.xml
3. 在web.xml中增加内容

编写UserLoginController.java文件，这个文件用于与浏览器（网页）交互控，制器代码如下：

```
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

```
spring-web.xml内容：

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:context="http://www.springframework.org/schema/context"
	xmlns:mvc="http://www.springframework.org/schema/mvc"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans.xsd
	http://www.springframework.org/schema/context
	http://www.springframework.org/schema/context/spring-context.xsd
	http://www.springframework.org/schema/mvc
	http://www.springframework.org/schema/mvc/spring-mvc-3.0.xsd">

	<!-- 配置SpringMVC -->
	<!-- 1.开启SpringMVC注解模式 -->
	<mvc:annotation-driven />

	<!-- 2.静态资源默认servlet配置-->
	<mvc:default-servlet-handler />
 

	<!-- 3.配置JSP   -->
	<bean
		class="org.springframework.web.servlet.view.InternalResourceViewResolver">
		<property name="viewClass"
			value="org.springframework.web.servlet.view.JstlView" />
		<property name="prefix" value="/" /><!-- 此处的斜杠表示从WebContent中访问网页 -->
		<property name="suffix" value=".jsp" />
	</bean>

	<!-- 4.扫描web相关的控制器类，这些类纳入spring的管理范围 -->
	<context:component-scan base-package="com.ychs.web" />
</beans>
```
web.xml内容：

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns="http://java.sun.com/xml/ns/javaee" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd" id="WebApp_ID" version="2.5">
	<!-- 配置DispatcherServlet -->
	<servlet>
		<servlet-name>spring-dispatcher</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<!-- 配置springMVC需要加载的配置文件，tomcat启动时加载，如果有错，服务启动可能报错，仔细观察
			spring-dao.xml,spring-service.xml,spring-web.xml			 
		 -->
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:spring/spring-*.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>spring-dispatcher</servlet-name>
		<!-- 默认匹配所有的请求 -->
		<url-pattern>/</url-pattern>
	</servlet-mapping>
	 
</web-app>
```
> ==控制层编写结束==，可以带网页测试了
### 八、网页测试
新建3个网页：login.jsp、success.jsp、fail.jsp，在WebContent下。
login.jsp代码：

```
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
```
success.jsp代码：

```<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录成功</title>
</head>
<body align=center>
	<h1>登录成功</h1>
</body>
</html>

```
fail.jsp代码：

```<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>登录失败</title>
</head>
<body align=center>
	<h1>登录失败</h1>
</body>
</html>

```
### 九、整体测试
1. 工程部署到Tomcat
2. 启动Tocmat
3. 浏览器输入网址测试，如下（我是SSH02，你的不一定是，注意更改）：
http://localhost:8080/SSM02/login.jsp
输入正确的用户名和密码看看显示；输入错误的在试试。
> #### 接下来的工作就是自己好好思考并整理一下整合思路，完全变成自己的。需要注意的是：框架整合开发需要多做、多写才能游刃有余，不要着急，坚持多写，成功就在那里等你。

> #### 交流探讨

```
public class Me {
	public String QQ = "44786501";
	public String email = "wuzhijun@ychs168.com";
}
```
![image](https://github.com/ychs168/SSM/blob/master/ychs.png)
