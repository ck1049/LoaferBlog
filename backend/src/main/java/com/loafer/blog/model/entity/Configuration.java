package com.loafer.blog.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("configuration")
public class Configuration {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String module;
    private String key;
    private String value;
    private String description;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableField("deleted")
    @TableLogic
    private Integer deleted;
    @TableField(value = "delete_time", fill = FieldFill.UPDATE)
    private LocalDateTime deleteTime;
}
