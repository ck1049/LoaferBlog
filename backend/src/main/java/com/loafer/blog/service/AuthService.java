package com.loafer.blog.service;

import com.loafer.blog.model.dto.LoginDTO;
import com.loafer.blog.model.dto.RegisterDTO;
import com.loafer.blog.model.dto.ChangePasswordDTO;
import com.loafer.blog.model.vo.LoginResponseVO;
import com.loafer.blog.model.vo.ResponseVO;

public interface AuthService {
    ResponseVO<Void> register(RegisterDTO registerDTO);
    ResponseVO<LoginResponseVO> login(LoginDTO loginDTO);
    ResponseVO<Void> logout();
    ResponseVO<Void> changePassword(Long userId, ChangePasswordDTO changePasswordDTO);
}