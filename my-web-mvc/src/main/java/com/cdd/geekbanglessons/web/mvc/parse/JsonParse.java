package com.cdd.geekbanglessons.web.mvc.parse;

/**
 * @author yangfengshan
 * @create 2021-03-02 14:56
 **/
public interface JsonParse {
    String toJSONString(Object object);

    <T> T parseObject(String json, Class<T> T);
}
