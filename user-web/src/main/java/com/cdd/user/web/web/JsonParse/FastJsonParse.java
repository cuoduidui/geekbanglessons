package com.cdd.user.web.web.JsonParse;

import com.alibaba.fastjson.JSON;
import com.cdd.geekbanglessons.web.mvc.parse.JsonParse;

/**
 * @author yangfengshan
 * @create 2021-03-02 15:16
 **/
public class FastJsonParse implements JsonParse {
    @Override
    public String toJSONString(Object object) {

        return JSON.toJSONString(object);
    }

    @Override
    public <T> T parseObject(String json, Class<T> T) {
        return JSON.parseObject(json, T);
    }
}
