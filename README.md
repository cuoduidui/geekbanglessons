# geekbanglessons
geekbanglessons

1、通过maven tomcat启动
2、
http://localhost/user-web/hello/register 进入注册页面
http://localhost/user-web/hello/login 登录页面
3、
com.cdd.user.web.web.listener.DBConnectionInitializerListener#contextInitialized默认读取jndi数据源 如果没有 则获取固定的数据源
com.cdd.user.web.web.listener.DBConnectionInitializerListener#contextDestroyed 关停所以数据库连接
4、com.cdd.user.web.sql.DBConnectionPoolManager 简单的 数据库连接池配置
