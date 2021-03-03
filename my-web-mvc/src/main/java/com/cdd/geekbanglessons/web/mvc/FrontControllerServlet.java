package com.cdd.geekbanglessons.web.mvc;

import com.cdd.geekbanglessons.web.mvc.controller.Controller;
import com.cdd.geekbanglessons.web.mvc.controller.PageController;
import com.cdd.geekbanglessons.web.mvc.controller.RestController;
import com.cdd.geekbanglessons.web.mvc.parse.DefaultJsonParse;
import com.cdd.geekbanglessons.web.mvc.parse.JsonParse;
import org.apache.commons.lang.StringUtils;

import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static java.util.Arrays.asList;

/**
 * @author yangfengshan
 * @create 2021-03-01 14:36
 **/
public class FrontControllerServlet extends HttpServlet {
    /**
     * 请求路径和 Controller 的映射关系缓存
     */
    private Map<String, Controller> controllersMapping = new HashMap<>();

    /**
     * 请求路径和 {@link HandlerMethodInfo} 映射关系缓存
     */
    private Map<String, HandlerMethodInfo> handleMethodInfoMapping = new HashMap<>();
    private JsonParse jsonParse = new DefaultJsonParse();

    /**
     * 初始化 Servlet
     *
     * @param servletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) {
        initHandleMethods();
        initJsonParse();
    }

    private void initJsonParse() {
        for (JsonParse localJsonParse : ServiceLoader.load(JsonParse.class)) {
            jsonParse = localJsonParse;
        }
    }

    /**
     * 读取所有的 RestController 的注解元信息 @Path
     * 利用 ServiceLoader 技术（Java SPI）
     */
    private void initHandleMethods() {
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<?> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String requestPath = pathFromClass.value();
            Method[] publicMethods = controllerClass.getMethods();
            // 处理方法支持的 HTTP 方法集合
            for (Method method : publicMethods) {
                Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                Path pathFromMethod = method.getAnnotation(Path.class);
                //不打注解的 过滤掉 小马哥写错了
                if (null == pathFromMethod) {
                    continue;
                }
                // 多个方法的时候 这个地方是错误的
                String path = requestPath + pathFromMethod.value();
                handleMethodInfoMapping.put(path,
                        new HandlerMethodInfo(path, method, supportedHttpMethods));
                controllersMapping.put(path, controller);
            }

        }
    }

    /**
     * 获取处理方法中标注的 HTTP方法集合
     *
     * @param method 处理方法
     * @return
     */
    private Set<String> findSupportedHttpMethods(Method method) {
        Set<String> supportedHttpMethods = new LinkedHashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            HttpMethod httpMethod = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }

        if (supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(asList(HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }

        return supportedHttpMethods;
    }

    /**
     * SCWCD
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 建立映射关系
        // requestURI = /a/hello/world
        String requestURI = request.getRequestURI();
        // contextPath  = /a or "/" or ""
        String servletContextPath = request.getContextPath();
        String prefixPath = servletContextPath;
        // 映射路径（子路径）
        String requestMappingPath = StringUtils.substringAfter(requestURI,
                StringUtils.replace(prefixPath, "//", "/"));
        // 映射到 Controller
        Controller controller = controllersMapping.get(requestMappingPath);

        if (controller != null) {

            HandlerMethodInfo handlerMethodInfo = handleMethodInfoMapping.get(requestMappingPath);

            try {
                if (handlerMethodInfo != null) {

                    String httpMethod = request.getMethod();

                    if (!handlerMethodInfo.getSupportedHttpMethods().contains(httpMethod)) {
                        // HTTP 方法不支持
                        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        return;
                    }
                    Method method = handlerMethodInfo.getHandlerMethod();
                    if (controller instanceof PageController) {
                        PageController pageController = PageController.class.cast(controller);
                        //执行注册方法
                        Object viewPath = method.invoke(pageController, request, response);
                        String viewPathStr = viewPath.toString();
                        // 页面请求 forward
                        // request -> RequestDispatcher forward
                        // RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
                        // ServletContext -> RequestDispatcher forward
                        // ServletContext -> RequestDispatcher 必须以 "/" 开头
                        ServletContext servletContext = request.getSession().getServletContext();
                        if (!viewPathStr.startsWith("/")) {
                            viewPathStr = "/" + viewPathStr;
                        }
                        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPathStr);
                        requestDispatcher.forward(request, response);
                        return;
                    } else if (controller instanceof RestController) {
                        response.setHeader("Content-type", "application/json;charset=UTF-8");
                        RestController restController = RestController.class.cast(controller);
                        Object json = method.invoke(restController, request, response);
                        System.out.println("执行方法:" + method.getName() + "返回参数：" + json);
                        PrintWriter printWriter = response.getWriter();
                        //json 框架选择
                        printWriter.write(jsonParse.parse(json));
                        printWriter.close();
                    }
                }
            } catch (Throwable throwable) {
                if (throwable.getCause() instanceof IOException) {
                    throw (IOException) throwable.getCause();
                } else {
                    throw new ServletException(throwable.getCause());
                }
            }
        }
    }

//    private void beforeInvoke(Method handleMethod, HttpServletRequest request, HttpServletResponse response) {
//
//        CacheControl cacheControl = handleMethod.getAnnotation(CacheControl.class);
//
//        Map<String, List<String>> headers = new LinkedHashMap<>();
//
//        if (cacheControl != null) {
//            CacheControlHeaderWriter writer = new CacheControlHeaderWriter();
//            writer.write(headers, cacheControl.value());
//        }
//    }
}
