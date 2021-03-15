# geekbanglessons

极客课后作业

## week-01

1、通过maven tomcat启动

2、http://localhost/user-web/hello/register 进入注册页面
http://localhost/user-web/hello/login 登录页面

3、com.cdd.user.web.web.listener.DBConnectionInitializerListener#contextInitialized默认读取jndi数据源 如果没有 则获取固定的数据源
com.cdd.user.web.web.listener.DBConnectionInitializerListener#contextDestroyed 关停所以数据库连接

4、com.cdd.user.web.sql.DBConnectionPoolManager 简单的 数据库连接池配置

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

​			访问属性：http://localhost/user-web/jolokia/read/com.cdd.user.web.web.Mbean:type=WebContext/ComponentContext

​			操作方法：http://localhost/user-web/jolokia/exec/com.cdd.user.web.web.Mbean:type=WebContext/toString

​			写入属性：http://localhost/user-web/jolokia/write/com.cdd.user.web.web.Mbean:type=WebContext/AppName/user-web