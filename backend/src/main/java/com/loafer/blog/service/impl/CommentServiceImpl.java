package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.loafer.blog.model.entity.Comment;
import com.loafer.blog.model.entity.Post;
import com.loafer.blog.model.entity.User;
import com.loafer.blog.mapper.CommentMapper;
import com.loafer.blog.mapper.PostMapper;
import com.loafer.blog.mapper.UserMapper;
import com.loafer.blog.model.vo.UserVO;
import com.loafer.blog.service.CommentService;
import com.loafer.blog.common.SensitiveWordFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private final SensitiveWordFilter sensitiveWordFilter;
    
    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserMapper userMapper;

    public CommentServiceImpl(SensitiveWordFilter sensitiveWordFilter) {
        this.sensitiveWordFilter = sensitiveWordFilter;
    }

    @Override
    public List<Comment> getCommentsByPostIdWithPagination(Long postId, Long parentId, Long lastCommentId, int size) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId)
                .eq("parent_id", parentId)
                .orderByDesc("create_time");
        
        // 如果提供了lastCommentId，则查询ID小于该值的记录
        if (lastCommentId != null) {
            wrapper.lt("id", lastCommentId);
        }
        
        // 限制返回记录数
        wrapper.last("LIMIT " + size);
        
        List<Comment> comments = baseMapper.selectList(wrapper);
        
        // 批量加载用户信息
        if (!comments.isEmpty()) {
            // 提取所有userId
            List<Long> userIds = comments.stream().map(Comment::getUserId).distinct().collect(Collectors.toList());
            // 批量查询用户
            List<User> users = userMapper.selectByIds(userIds);
            // 转换为Map，方便查找
            Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
            // 为每个评论设置用户信息（转换为UserVO）
            for (Comment comment : comments) {
                User user = userMap.get(comment.getUserId());
                if (user != null) {
                    comment.setUser(new UserVO(user));
                }
            }
        }
        
        return comments;
    }

    @Override
    public int getCommentsCountByPostId(Long postId, Long parentId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("post_id", postId)
                .eq("parent_id", parentId);
        return baseMapper.selectCount(wrapper).intValue();
    }

    @Override
    public Comment createComment(Comment comment) {
        // 敏感词过滤
        String originalContent = comment.getContent();
        String filteredContent = sensitiveWordFilter.filter(originalContent);
        comment.setOriginalContent(originalContent);
        comment.setContent(filteredContent);
        if (comment.getParentId() == null) {
            comment.setParentId(0L);
        }

        baseMapper.insert(comment);
        
        // 加载用户信息并转换为UserVO
        User user = userMapper.selectById(comment.getUserId());
        if (user != null) {
            comment.setUser(new UserVO(user));
        }
        
        // 更新帖子评论数（所有评论都计入，包括回复）
        Post post = postMapper.selectById(comment.getPostId());
        if (post != null) {
            UpdateWrapper<Post> wrapper = new UpdateWrapper<>();
            wrapper.eq("id", comment.getPostId())
                    .set("comment_count", post.getCommentCount() != null ? post.getCommentCount() + 1 : 1);
            postMapper.update(null, wrapper);
        }
        
        return comment;
    }

    @Override
    public boolean deleteComment(Long id) {
        // 获取评论信息
        Comment comment = baseMapper.selectById(id);
        if (comment == null) {
            return false;
        }
        
        // 先删除回复
        QueryWrapper<Comment> replyWrapper = new QueryWrapper<>();
        replyWrapper.eq("parent_id", id);
        int replyCount = baseMapper.delete(replyWrapper);
        
        // 再删除评论
        boolean result = baseMapper.deleteById(id) > 0;
        
        // 更新帖子评论数（所有评论都计入，包括回复）
        if (result) {
            Post post = postMapper.selectById(comment.getPostId());
            if (post != null) {
                // 计算要减少的评论数（当前评论 + 所有回复）
                int totalDeleteCount = 1 + replyCount;
                UpdateWrapper<Post> wrapper = new UpdateWrapper<>();
                wrapper.eq("id", comment.getPostId())
                        .set("comment_count", Math.max(0, post.getCommentCount() != null ? post.getCommentCount() - totalDeleteCount : 0));
                postMapper.update(null, wrapper);
            }
        }
        
        return result;
    }

    @Override
    public List<Comment> getRepliesByCommentId(Long commentId) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", commentId)
                .orderByAsc("create_time");
        
        List<Comment> replies = baseMapper.selectList(wrapper);
        
        // 批量加载用户信息
        if (!replies.isEmpty()) {
            // 提取所有userId
            List<Long> userIds = replies.stream().map(Comment::getUserId).distinct().collect(Collectors.toList());
            // 批量查询用户
            List<User> users = userMapper.selectBatchIds(userIds);
            // 转换为Map，方便查找
            java.util.Map<Long, User> userMap = users.stream().collect(Collectors.toMap(User::getId, user -> user));
            // 为每个回复设置用户信息（转换为UserVO）
            for (Comment reply : replies) {
                User user = userMap.get(reply.getUserId());
                if (user != null) {
                    reply.setUser(new UserVO(user));
                }
            }
        }
        
        return replies;
    }
}
