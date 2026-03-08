package com.loafer.blog.vo;

import lombok.Data;

@Data
public class ResponseVO<T> {
    private int code;
    private String message;
    private T data;

    public ResponseVO() {
    }

    public ResponseVO(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseVO(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseVO<T> success() {
        return new ResponseVO<>(200, "操作成功");
    }

    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>(200, "操作成功", data);
    }

    public static <T> ResponseVO<T> success(String message, T data) {
        return new ResponseVO<>(200, message, data);
    }

    public static <T> ResponseVO<T> error(int code, String message) {
        return new ResponseVO<>(code, message);
    }

    public static <T> ResponseVO<T> error(String message) {
        return new ResponseVO<>(500, message);
    }
}
