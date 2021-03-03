package com.cdd.user.web.web.controller;

import com.cdd.constants.Constants;
import com.cdd.datastandard.Response;
import com.cdd.geekbanglessons.web.mvc.controller.RestController;
import com.cdd.user.web.domain.User;
import com.cdd.user.web.repository.DatabaseUserRepository;
import com.cdd.user.web.repository.UserRepository;
import com.cdd.user.web.sql.DBConnectionManager;
import com.cdd.user.web.sql.DBConnectionPoolManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 登录接口
 *
 * @author yangfengshan
 * @create 2021-03-01 15:40
 **/
@Path("/login")
public class LoginController implements RestController {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        return null;
    }

    @Path("/register")
    @GET
    @POST
    public Response register(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        Map<String, String[]> parameterMap = request.getParameterMap();
        User user = new User();
        user.setEmail(getFristData(parameterMap.get("email")));
        user.setName(getFristData(parameterMap.get("name")));
        user.setPassword(getFristData(parameterMap.get("password")));
        user.setPhoneNumber(getFristData(parameterMap.get("phoneNumber")));
//        String databaseURL = "jdbc:derby:/db/user-platform;create=true";
//        Connection connection = DriverManager.getConnection(databaseURL);
//        DBConnectionManager manager = new DBConnectionManager();
//        manager.setConnection(connection);
        DBConnectionManager manager = DBConnectionPoolManager.getDBConnectionManager();
        UserRepository userRepository = new DatabaseUserRepository(manager);
        boolean isTrue = userRepository.save(user);
        DBConnectionPoolManager.returnDBConnectionManager(manager);
        if (isTrue) {
            return Response.buildSuccessResponse(user);
        }
        return Response.buildFailResponse(Constants.ResponseCode.UNKNOWN_ERROR, Constants.ResponseInfo.UNKNOWN_ERROR, Constants.ResponseInfo.UNKNOWN_ERROR);
    }

    public String getFristData(String[] datas) {
        if (null == datas || datas.length == 0) return null;
        return datas[0];
    }

    public static void main(String[] args) {
        ServiceLoader<UserRepository> controller = ServiceLoader.load(UserRepository.class);
        controller.forEach(c -> System.out.println(c.getClass().getName()));
    }
}
