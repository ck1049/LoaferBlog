package com.loafer.blog.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.loafer.blog.model.vo.UserVO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;
    @TableField("post_id")
    private Long postId;
    @TableField("user_id")
    private Long userId;
    @TableField("parent_id")
    private Long parentId;
    private String content;
    @TableField("original_content")
    private String originalContent;
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime deleteTime;
    
    // 关联用户信息（非数据库字段）
    @TableField(exist = false)
    private UserVO user;
}