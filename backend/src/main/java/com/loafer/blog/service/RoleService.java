package com.loafer.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.loafer.blog.model.dto.RoleDTO;
import com.loafer.blog.model.entity.Role;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.RoleVO;

import java.util.List;

public interface RoleService extends IService<Role> {
    ResponseVO<List<RoleVO>> getRoles();
    ResponseVO<RoleVO> createRole(RoleDTO roleDTO);
    ResponseVO<RoleVO> updateRole(Long id, RoleDTO roleDTO);
    ResponseVO<Void> deleteRole(Long id);
}