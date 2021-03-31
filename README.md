# geekbanglessons

极客课后作业

## week-01

1、通过maven tomcat启动

2、http://localhost/user-web/hello/register 进入注册页面
http://localhost/user-web/hello/login 登录页面

3、``com.cdd.user.web.web.listener.DBConnectionInitializerListener#contextInitialized``默认读取jndi数据源 如果没有 则获取固定的数据源
``com.cdd.user.web.web.listener.DBConnectionInitializerListener#contextDestroyed ``关停所以数据库连接

4、``com.cdd.user.web.sql.DBConnectionPoolManager`` 简单的 数据库连接池配置

## week-02

要求：
    1、实现简单的依赖注入

​    2、实现简单的参数校验

​        1、id>0的整数

​        2、密码 6-32位

​        3、电话号码 中国大陆方式登录

​    3、jpa是整合

登录地址:http://127.0.0.1/user-web/hello/register

## week-03

1、整合jolokia：

​		启动项目后访问：http://localhost/user-web/jolokia/read/java.lang:type=Memory/HeapMemoryUsage 查询内存使用情况

​		自定义bean访问：

​			自定义jmx bean（com.cdd.user.web.web.Mbean.WebContext）

​			访问属性：http://localhost/user-web/jolokia/read/com.cdd.user.web.web.Mbean:type=WebContext/ComponentContext

​			操作方法：http://localhost/user-web/jolokia/exec/com.cdd.user.web.web.Mbean:type=WebContext/toString

​			写入属性：http://localhost/user-web/jolokia/write/com.cdd.user.web.web.Mbean:type=WebContext/AppName/user-web

​			查询所有暴露的bean：http://localhost/user-web/jolokia/list

2、实现``Converter``、``ConfigSource``

​	自定义Converter：``com.cdd.user.web.web.config.converter.StringToIntegerConverter``

​	自定义ConfigSource：``com.cdd.user.web.web.config.source.PropertiesConfigSource``

​	访问路径：http://localhost/user-web/login/getWebInfo   获取配置文件``META-INF/config/config.properties``中的appId 和AppName并把appId String 转换成Integer

## week-04

1、整合my-dependency-injection模块

​	``com.cdd.geekbanglessons.web.mvc.initializer.MyWebMvcServletContainerInitializer`` 自定义``ServletContainerInitializer`` 并通过@HandlesTypes指定自定义初始化接口``MyWebMvcInitializer``进行配置元信息、依赖注入、Servlet信息注入到Servlet上下文中

``com.cdd.geekbanglessons.web.mvc.initializer.FrontControllerServletInitializer``  Servlet监听添加

``com.cdd.geekbanglessons.web.mvc.initializer.ConfigInitializer`` 配置信息初始化

``com.cdd.geekbanglessons.web.mvc.initializer.ComponentContextInitializer`` 依赖注入初始化

``com.cdd.user.web.web.listener.UserWebInitializer`` web自定义信息初始化 拦截器（``CharsetEncodingFilter``）、监听器（``ComponentContextMbeanListener``、``DBConnectionInitializerListener``）

``com.cdd.user.web.web.listener.ConfigServletRequestListener`` 配置信息请求拦截器---写入``ThreadLocal``

请求地址：http://localhost/user-web/login/getWebConfig 获取当前请求线程中所以配置

请求地址：http://localhost/user-web/login/getWebInfo 获取appName appId

备注：
    ``HandlesTypes``注解不同情况下的区分：
        ``Tomcat determines if a directory is an expanded JAR file by looking for a META-INF sub-directory.
        Only if the META-INF sub-directory exists, the directory is assumed to be an expanded JAR file.
        Note that for scans for matches to @HandlesTypes annotations,
        all directories will be scanned irrespective of the presence or not of a META-INF sub-directory``



## week-05

1、补全post方法：

​	主要补全方法：``com.cdd.rest.client.DefaultInvocationBuilder#post(javax.ws.rs.client.Entity<?>)``

​								``com.cdd.rest.client.DefaultInvocationBuilder#buildPost``

​								``com.cdd.rest.client.HttpPostInvocation`` 增加``PostInvocation``实现类

​								测试方法：1、启动项目

​													2、运行测试类``com.cdd.rest.demo.RestClientDemo#main``

标注：本次解决``com.cdd.user.web.orm.jpa.DelegatingEntityManager`` bean创建失败的bug : ```persistence.xml``` version 版本导致  2.2->2.1