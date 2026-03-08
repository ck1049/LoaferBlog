package com.loafer.blog.service;

import com.loafer.blog.model.dto.RoleDTO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.RoleVO;

import java.util.List;

public interface RoleService {
    ResponseVO<List<RoleVO>> getRoles();
    ResponseVO<RoleVO> createRole(RoleDTO roleDTO);
    ResponseVO<RoleVO> updateRole(Long id, RoleDTO roleDTO);
    ResponseVO<Void> deleteRole(Long id);
}