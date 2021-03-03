package com.cdd.geekbanglessons.web.mvc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 控制器接口
 *
 * @author yangfengshan
 * @create 2021-03-01 14:34
 **/
public interface Controller {

    /**
     * @param request  HTTP 请求
     * @param response HTTP 相应
     * @return 视图地址路径
     * @throws Throwable 异常发生时
     */
    String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable;
}
