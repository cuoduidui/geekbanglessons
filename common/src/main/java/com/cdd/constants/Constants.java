package com.cdd.constants;

/**
 * @author yangfengshan
 * @create 2021-03-02 10:08
 **/
public class Constants {
    public static final class ResponseCode {
        public static final String SUCCESS = "0000";
        public static final String UNKNOWN_ERROR = "0001";
        public static final String INDEX_DUP = "0002";
        public static final String PARAMETER_WRONGFUL = "0003";
        public static final String NONEXISTENT = "0004";
        public static final String NO_CHANGE = "0101";

    }

    public static final class ResponseInfo {
        public static final String SUCCESS = "成功";
        public static final String UNKNOWN_ERROR = "未知错误";
        public static final String INDEX_DUP = "唯一索引冲突";
        public static final String PARAMETER_WRONGFUL = "参数不合法";
        public static final String NONEXISTENT = "不存在的";
        public static final String NO_CHANGE = "没有任何变动";
    }
}
