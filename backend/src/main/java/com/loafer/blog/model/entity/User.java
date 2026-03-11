package com.loafer.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("\"user\"")
public class User {
    @TableId(type = IdType.AUTO)
    @TableField("id")
    private Long id;
    @TableField("username")
    private String username;
    @TableField("password")
    private String password;
    @TableField("email")
    private String email;
    @TableField("nickname")
    private String nickname;
    @TableField("avatar")
    private String avatar;
    @TableField("bio")
    private String bio;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableField("status")
    private Integer status;
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
}