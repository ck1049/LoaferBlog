package com.loafer.blog.model.vo;

import lombok.Data;

@Data
public class ResponseVO<T> {
    private int code;
    private String message;
    private T data;

    public ResponseVO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>(200, "success", data);
    }

    public static <T> ResponseVO<T> error(String message) {
        return new ResponseVO<>(400, message, null);
    }
}
