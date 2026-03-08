package com.loafer.blog.service;

import com.loafer.blog.model.dto.UserDTO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    ResponseVO<UserVO> getCurrentUser(Long userId);
    ResponseVO<UserVO> updateCurrentUser(Long userId, UserDTO userDTO);
    ResponseVO<Void> deleteAccount(Long userId);
    ResponseVO<UserVO> uploadAvatar(Long userId, MultipartFile avatar);
}
