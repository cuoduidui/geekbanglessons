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