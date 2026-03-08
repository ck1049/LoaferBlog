package com.loafer.blog.controller;

import com.loafer.blog.model.dto.UserDTO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.UserVO;
import com.loafer.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

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
}
