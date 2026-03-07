package com.loafer.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.loafer.blog.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TagMapper extends BaseMapper<Tag> {
}
