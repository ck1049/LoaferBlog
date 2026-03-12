package com.loafer.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.model.entity.User;
import com.loafer.blog.model.vo.UserVO;
import com.loafer.blog.service.UserService;
import com.loafer.blog.utils.SensitiveInfoUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/search")
public class UserSearchController {

    private final UserService userService;

    public UserSearchController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 搜索用户
     * @param username 用户名
     * @param lastId 上一页的最大用户 ID
     * @param size 每页大小
     * @return 用户列表
     */
    @GetMapping
    public List<UserVO> searchUsers(
            @RequestParam String username,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "10") Integer size) {

        QueryWrapper<User> wrapper = new QueryWrapper<>();
        // 按照用户名模糊搜索
        wrapper.like("username", username);
        // 只查询未删除的用户
        wrapper.eq("deleted", 0);
        // 如果提供了 lastId，则查询 ID 大于 lastId 的用户
        if (lastId != null) {
            wrapper.gt("id", lastId);
        }
        // 按照用户 ID 排序
        wrapper.orderByAsc("id");
        // 限制返回数量
        wrapper.last("LIMIT " + size);

        // 查询用户
        List<User> users = userService.list(wrapper);

        // 转换为 UserVO
        return users.stream().map(user -> {
            UserVO userVO = new UserVO();
            userVO.setId(user.getId());
            userVO.setUsername(user.getUsername());
            userVO.setNickname(user.getNickname());
            userVO.setAvatar(user.getAvatar());
            return userVO;
        }).collect(Collectors.toList());
    }
}
