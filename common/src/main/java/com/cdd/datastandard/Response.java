package com.cdd.datastandard;

import com.cdd.constants.Constants;

/**
 * @author yangfengshan
 * @create 2021-03-02 10:03
 **/
public class Response<T> implements java.io.Serializable {
    private String code;
    private String msg;
    private String info;
    private T data;
    private boolean success;

    public static <T> Response<T> buildSuccessResponse() {
        return Response.buildSuccessResponse(null, Constants.ResponseCode.SUCCESS, Constants.ResponseInfo.SUCCESS, Constants.ResponseInfo.SUCCESS);
    }

    public static <T> Response<T> buildSuccessResponse(T data, String code, String msg, String info) {
        return Response.buildResponse(data, code, msg, info, true);
    }

    public static <T> Response<T> buildSuccessResponse(T data) {
        return Response.buildSuccessResponse(data, Constants.ResponseCode.SUCCESS, Constants.ResponseInfo.SUCCESS, Constants.ResponseInfo.SUCCESS);
    }

    public static <T> Response<T> buildFailResponse(String code, String msg, String info) {
        return Response.buildResponse(null, code, msg, info, true);
    }

    public static <T> Response<T> buildErrorResponse(String code, String msg, String info) {
        return Response.buildResponse(null, code, msg, info, false);
    }

    public static <T> Response<T> buildResponse(T data, String code, String msg, String info, boolean success) {
        Response response = new Response();
        response.setCode(Constants.ResponseCode.SUCCESS);
        response.setData(data);
        response.setInfo(Constants.ResponseInfo.SUCCESS);
        response.setMsg(Constants.ResponseInfo.SUCCESS);
        response.setSuccess(success);
        return response;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
