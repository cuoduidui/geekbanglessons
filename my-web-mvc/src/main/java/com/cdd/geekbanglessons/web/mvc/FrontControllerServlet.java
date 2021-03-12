package com.cdd.geekbanglessons.web.mvc;

import com.cdd.datastandard.Response;
import com.cdd.geekbanglessons.web.mvc.context.ComponentContext;
import com.cdd.geekbanglessons.web.mvc.controller.Controller;
import com.cdd.geekbanglessons.web.mvc.controller.PageController;
import com.cdd.geekbanglessons.web.mvc.controller.RestController;
import com.cdd.geekbanglessons.web.mvc.parse.DefaultJsonParse;
import com.cdd.geekbanglessons.web.mvc.parse.JsonParse;
import com.cdd.geekbanglessons.web.mvc.valid.annotation.DataValid;
import org.apache.commons.lang.StringUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
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

    private ComponentContext context;

    /**
     * 初始化 Servlet
     *
     * @param servletConfig
     */
    @Override
    public void init(ServletConfig servletConfig) {
        ComponentContext componentContext = new ComponentContext();
        componentContext.init(servletConfig.getServletContext());
        context = componentContext;
        initHandleMethods();
        initJsonParse();

    }

    @Override
    public void destroy() {
        context.destroy();
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
        for (String controllerName : context.getComponentNames()) {
            Object controller = context.getComponent(controllerName);
            if (controller instanceof Controller) {
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
//                    injectComponents(controller, controller.getClass());
                    controllersMapping.put(path, (Controller) controller);
                }
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
                        //todo 可以抽出  参数填充 参数校验
                        Class<?>[] clazzs = method.getParameterTypes();
                        Object json = null;
                        if (null == clazzs || clazzs.length <= 0) {
                            json = method.invoke(restController);
                        } else {
                            Map<String, String[]> parameterMap = request.getParameterMap();
//                           todo 参数转换 目前只支持一个参数
                            Class<?> clazz = clazzs[0];
                            Object object = mapTransformObject(clazz, parameterMap);
                            Annotation[][] annotations = method.getParameterAnnotations();
                            Annotation[] annotations1 = annotations[0];
                            if (annotations1.length > 0) {
                                Annotation annotation = annotations1[0];
                                Boolean isTrue = true;
                                Response responseData = null;
                                if (annotation.annotationType().equals(DataValid.class)) {
                                    Validator validator = context.getComponent("bean/Validator");
                                    if (null != validator) {
                                        Set<ConstraintViolation<Object>> violations = validator.validate(object);
                                        for (ConstraintViolation<Object> objectConstraintViolation : violations) {
                                            String msg = objectConstraintViolation.getMessage();
                                            if (!StringUtils.isEmpty(msg)) {
                                                isTrue = false;
                                                responseData = Response.buildFailResponse("D0001", msg, "参数错误");
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (!isTrue) {
                                    json = responseData;
                                } else {
                                    json = method.invoke(restController, object);
                                }
                            } else {
                                json = method.invoke(restController, object);
                            }
                        }
                        System.out.println("执行方法:" + method.getName() + "返回参数：" + json);
                        PrintWriter printWriter = response.getWriter();
                        //json 框架选择
                        printWriter.write(jsonParse.toJSONString(json));
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

    private <T> T mapTransformObject(Class<T> T, Map<String, String[]> parameterMap) {
        Map<String, String> parameterObjectMap = new HashMap<>(parameterMap.size(), 1);
        //简单实现
        parameterMap.forEach((k, v) -> {
            parameterObjectMap.put(k, v == null ? null : v[0]);
        });
        String json = jsonParse.toJSONString(parameterObjectMap);
        return jsonParse.parseObject(json, T);
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
