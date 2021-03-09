package com.cdd.geekbanglessons.web.mvc.parse;

/**
 * @author yangfengshan
 * @create 2021-03-02 14:58
 **/
public class DefaultJsonParse implements JsonParse {

    @Override
    public String toJSONString(Object object) {
        return object.toString();
    }

    @Override
    public <T> T parseObject(String json, Class<T> T) {
        return null;
    }
}
