package com.shj.apiserver.entity;

import lombok.Data;

import java.util.Map;

@Data
public class ApiResponse<T> {
    private Integer code;
    private T data;
    private String msg;
    private Map<String, Object> others;

    public static <T> ApiResponse<T> success(T data, String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(data);
        response.setMsg(msg);
        return response;
    }

    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(200);
        response.setData(data);
        response.setMsg("ok");
        return response;
    }

    public static <T> ApiResponse<T> error(Integer code, String msg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }
}
