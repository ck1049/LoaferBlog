package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.model.dto.UserDTO;
import com.loafer.blog.model.entity.User;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService extends IService<User> {
    ResponseVO<UserVO> getCurrentUser(Long userId);
    ResponseVO<UserVO> updateCurrentUser(Long userId, UserDTO userDTO);
    ResponseVO<Void> deleteAccount(Long userId);
    ResponseVO<UserVO> uploadAvatar(Long userId, MultipartFile avatar);
}
