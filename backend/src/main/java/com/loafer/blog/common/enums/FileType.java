package com.loafer.blog.common.enums;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文件类型枚举
 * @author loafer
 * @since 2026-03-12 21:21:25
 **/
@Getter
@AllArgsConstructor
public enum FileType {
    IMAGE("image"),
    VIDEO("video"),
    OTHER("other");
    private final String type;

    /**
     * 根据媒体类型获取文件类型
     * @param mediaType 媒体类型
     * @return 文件类型
     */
    public static FileType getByMediaType(@NotNull String mediaType) {
        for (FileType value : values()) {
            if (mediaType.startsWith(value.type)) {
                return value;
            }
        }
        return OTHER;
    }
}
