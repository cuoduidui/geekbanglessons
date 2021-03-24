package com.cdd.dependency.injection.context;


import com.cdd.dependency.injection.configuration.annotation.ConfigValue;
import com.cdd.function.ThrowableAction;
import com.cdd.function.ThrowableFunction;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.naming.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * 组件上下文（Web 应用全局使用）
 */
public class ComponentContext {

    public static final String CONTEXT_NAME = ComponentContext.class.getName();

    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

    private static final Logger logger = Logger.getLogger(CONTEXT_NAME);

    private static final ComponentContext componentContext = new ComponentContext();
    // 假设一个 Tomcat JVM 进程，三个 Web Apps，会不会相互冲突？（不会冲突）
    // static 字段是 JVM 缓存吗？（是 ClassLoader 缓存）

//    private static ApplicationContext applicationContext;

//    public void setApplicationContext(ApplicationContext applicationContext){
//        ComponentContext.applicationContext = applicationContext;
//        WebApplicationContextUtils.getRootWebApplicationContext()
//    }

    private Context envContext; // Component Env Context

    private ClassLoader classLoader;
    private Config config;

    private Map<String, Object> componentsMap = new LinkedHashMap<>();

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * 获取 ComponentContext
     *
     * @return
     */
    public static ComponentContext getInstance() {
        return componentContext;
    }

    private static void close(Context context) {
        if (context != null) {
            ThrowableAction.execute(context::close);
        }
    }

    public void init() throws RuntimeException {
        initConfig();
        initEnvContext();
        instantiateComponents();
        initializeComponents();
    }

    private void initConfig() {
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        if (null == classLoader) classLoader = Thread.currentThread().getContextClassLoader();
        config = configProviderResolver.getConfig(classLoader);
        if (config == null) {
            ConfigBuilder configBuilder = configProviderResolver.getBuilder();
            // 配置 ClassLoader
            configBuilder.forClassLoader(classLoader);
            // 默认配置源（内建的，静态的）
            configBuilder.addDefaultSources();
            // 通过发现配置源（动态的）
            configBuilder.addDiscoveredConverters();
            // 获取 Config
            Config config = configBuilder.build();
            // 注册 Config 关联到当前 ClassLoader
            configProviderResolver.registerConfig(config, classLoader);
        }
    }


    /**
     * 实例化组件
     */
    protected void instantiateComponents() {
        // 遍历获取所有的组件名称
        List<String> componentNames = listAllComponentNames();
        // 通过依赖查找，实例化对象（ Tomcat BeanFactory setter 方法的执行，仅支持简单类型）
        componentNames.forEach(name -> componentsMap.put(name, lookupComponent(name)));
    }

    /**
     * 初始化组件（支持 Java 标准 Commons Annotation 生命周期）
     * <ol>
     * <li>注入阶段 - {@link Resource}</li>
     * <li>初始阶段 - {@link PostConstruct}</li>
     * <li>销毁阶段 - {@link PreDestroy}</li>
     * </ol>
     */
    protected void initializeComponents() {
        componentsMap.values().forEach(component -> {
            Class<?> componentClass = component.getClass();
            // 注入阶段 - {@link Resource}
            injectComponents(component, componentClass);
            // 初始阶段 - {@link PostConstruct}
//            processPostConstruct(component, componentClass);
        });
    }

    private void injectComponents(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getDeclaredFields())
                .filter(field -> {
                    int mods = field.getModifiers();
                    return !Modifier.isStatic(mods);
                }).forEach(field -> {
            if (field.isAnnotationPresent(Resource.class)) {
                handleResource(component, field);
            } else if (field.isAnnotationPresent(ConfigValue.class)) {
                if (null == config) return;
                handleResourceConfigValue(component, field);
            }
        });
    }

    private void handleResourceConfigValue(Object component, Field field) {
        ConfigValue configValue = field.getAnnotation(ConfigValue.class);
        String propertyName = configValue.value();
        Object injectedObject = config.getValue(propertyName, field.getType());
        field.setAccessible(true);
        try {
            // 注入目标对象
            field.set(component, injectedObject);
        } catch (IllegalAccessException e) {
        }
    }

    void handleResource(Object component, Field field) {
        Resource resource = field.getAnnotation(Resource.class);
        String resourceName = resource.name();
        Object injectedObject = lookupComponent(resourceName);
        field.setAccessible(true);
        try {
            // 注入目标对象
            field.set(component, injectedObject);
        } catch (IllegalAccessException e) {
        }
    }

    private void processPostConstruct(Object component, Class<?> componentClass) {
        Stream.of(componentClass.getMethods())
                .filter(method ->
                        !Modifier.isStatic(method.getModifiers()) &&      // 非 static
                                method.getParameterCount() == 0 &&        // 没有参数
                                method.isAnnotationPresent(PostConstruct.class) // 标注 @PostConstruct
                ).forEach(method -> {
            // 执行目标方法
            try {
                method.invoke(component);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function ThrowableFunction
     * @param <R>      返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function) {
        return executeInContext(function, false);
    }

    /**
     * 在 Context 中执行，通过指定 ThrowableFunction 返回计算结果
     *
     * @param function         ThrowableFunction
     * @param ignoredException 是否忽略异常
     * @param <R>              返回结果类型
     * @return 返回
     * @see ThrowableFunction#apply(Object)
     */
    protected <R> R executeInContext(ThrowableFunction<Context, R> function, boolean ignoredException) {
        return executeInContext(this.envContext, function, ignoredException);
    }

    private <R> R executeInContext(Context context, ThrowableFunction<Context, R> function,
                                   boolean ignoredException) {
        R result = null;
        try {
            result = ThrowableFunction.execute(context, function);
        } catch (Throwable e) {
            if (ignoredException) {
                logger.warning(e.getMessage());
            } else {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public <C> C lookupComponent(String name) {
        return executeInContext(context -> (C) context.lookup(name));
    }

    /**
     * 通过名称进行依赖查找
     *
     * @param name
     * @param <C>
     * @return
     */
    public <C> C getComponent(String name) {
        return (C) componentsMap.get(name);
    }

    /**
     * 获取所有的组件名称
     *
     * @return
     */
    public List<String> getComponentNames() {
        return new ArrayList<>(componentsMap.keySet());
    }

    private List<String> listAllComponentNames() {
        return listComponentNames("/");
    }

    protected List<String> listComponentNames(String name) {
        return executeInContext(context -> {
            NamingEnumeration<NameClassPair> e = executeInContext(context, ctx -> ctx.list(name), true);

            // 目录 - Context
            // 节点 -
            if (e == null) { // 当前 JNDI 名称下没有子节点
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair element = e.nextElement();
                String className = element.getClassName();
                Class<?> targetClass = classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(targetClass)) {
                    // 如果当前名称是目录（Context 实现类）的话，递归查找
                    fullNames.addAll(listComponentNames(element.getName()));
                } else {
                    // 否则，当前名称绑定目标类型的话话，添加该名称到集合中
                    String fullName = name.startsWith("/") ?
                            element.getName() : name + "/" + element.getName();
                    fullNames.add(fullName);
                }
            }
            return fullNames;
        });
    }

    public void destroy() throws RuntimeException {
        close(this.envContext);
    }

    private void initEnvContext() throws RuntimeException {
        if (this.envContext != null) {
            return;
        }
        Context context = null;
        try {
            context = new InitialContext();
            this.envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            close(context);
        }
    }
}
