package com.loafer.blog.model.vo;

import com.loafer.blog.utils.FileUploadUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FriendVO {
    private Long id;
    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    private LocalDateTime createTime;

    public String getAvatar() {
        return FileUploadUtils.spliceUrl(this.avatar);
    }
}