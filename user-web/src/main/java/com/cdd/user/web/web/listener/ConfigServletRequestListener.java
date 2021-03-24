package com.cdd.user.web.web.listener;

import com.cdd.geekbanglessons.web.mvc.config.ConfigUtil;
import org.eclipse.microprofile.config.Config;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

/**
 * @author yangfengshan
 * @create 2021-03-24 17:29
 **/
public class ConfigServletRequestListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        Config config = ConfigUtil.getConfigThreadLocal();
        if (null != config)
            ConfigUtil.removeConfigThreadLocal(config);
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        try {
            ServletContext servletContext = sre.getServletContext();
            //获取上下文的配置信息
            Config config = ConfigUtil.getConfig(servletContext.getClassLoader());
            ConfigUtil.setConfigThreadLocal(config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
