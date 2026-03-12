package com.loafer.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.loafer.blog.model.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    
    IPage<Post> searchPostsJieba(Page<Post> page, @Param("keyword") String keyword);
    
    IPage<Post> searchPostsLike(Page<Post> page, @Param("keyword") String keyword);
}