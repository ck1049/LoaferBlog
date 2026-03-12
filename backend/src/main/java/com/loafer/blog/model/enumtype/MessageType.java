package com.loafer.blog.model.enumtype;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum MessageType {
    TEXT(1, "文本消息"),
    EMOJI(2, "表情消息"),
    IMAGE(3, "图片消息"),
    VIDEO(4, "视频消息"),
    FILE(5, "文件消息");

    @JsonValue
    private final Integer code;
    private final String desc;

    MessageType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @JsonCreator
    public static MessageType getByCode(Integer code) {
        for (MessageType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return TEXT;
    }

    @Override
    public String toString() {
        return code.toString();
    }
}