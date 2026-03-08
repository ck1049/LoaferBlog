package com.loafer.blog.controller;

import com.loafer.blog.model.dto.PostWithTimeDTO;
import com.loafer.blog.model.dto.UserDTO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.UserVO;
import com.loafer.blog.service.UserService;
import com.loafer.blog.service.PostInteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private PostInteractionService postInteractionService;

    @GetMapping("/me")
    public ResponseVO<UserVO> getCurrentUser(@RequestAttribute("userId") Long userId) {
        return userService.getCurrentUser(userId);
    }

    @PutMapping("/me")
    public ResponseVO<UserVO> updateCurrentUser(@RequestAttribute("userId") Long userId, @RequestBody UserDTO userDTO) {
        return userService.updateCurrentUser(userId, userDTO);
    }



    @DeleteMapping("/me")
    public ResponseVO<Void> deleteAccount(@RequestAttribute("userId") Long userId) {
        return userService.deleteAccount(userId);
    }
    
    @PostMapping("/avatar")
    public ResponseVO<UserVO> uploadAvatar(@RequestAttribute("userId") Long userId, @RequestParam("avatar") MultipartFile avatar) {
        return userService.uploadAvatar(userId, avatar);
    }
    
    // 获取用户的点赞列表
    @GetMapping("/likes")
    public ResponseVO<?> getLikedPosts(@RequestAttribute("userId") Long userId, 
                                     @RequestParam(defaultValue = "1") Integer page, 
                                     @RequestParam(defaultValue = "10") Integer size) {
        return postInteractionService.getLikedPosts(userId, page, size);
    }
    
    // 获取用户的收藏列表
    @GetMapping("/favorites")
    public ResponseVO<?> getFavoritedPosts(@RequestAttribute("userId") Long userId, 
                                         @RequestParam(defaultValue = "1") Integer page, 
                                         @RequestParam(defaultValue = "10") Integer size) {
        return postInteractionService.getFavoritedPosts(userId, page, size);
    }
    
    // 获取用户的浏览历史
    @GetMapping("/history")
    public ResponseVO<?> getViewHistory(@RequestAttribute("userId") Long userId, 
                                      @RequestParam(defaultValue = "1") Integer page, 
                                      @RequestParam(defaultValue = "10") Integer size) {
        return postInteractionService.getViewHistory(userId, page, size);
    }
    
    // 删除用户的浏览历史
    @DeleteMapping("/history/{postId}")
    public ResponseVO<Void> deleteViewHistory(@RequestAttribute("userId") Long userId, @PathVariable Long postId) {
        return postInteractionService.deleteViewHistory(userId, postId);
    }
}
