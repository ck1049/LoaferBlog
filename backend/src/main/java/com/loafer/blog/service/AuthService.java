package com.loafer.blog.service;

import com.loafer.blog.dto.LoginDTO;
import com.loafer.blog.dto.RegisterDTO;
import com.loafer.blog.vo.LoginResponseVO;
import com.loafer.blog.vo.ResponseVO;
import com.loafer.blog.vo.UserVO;

public interface AuthService {
    ResponseVO<Void> register(RegisterDTO registerDTO);
    ResponseVO<LoginResponseVO> login(LoginDTO loginDTO);
    ResponseVO<Void> logout();
    ResponseVO<UserVO> getCurrentUser(Long userId);
}