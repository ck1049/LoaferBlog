package com.loafer.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loafer.blog.model.entity.Message;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
