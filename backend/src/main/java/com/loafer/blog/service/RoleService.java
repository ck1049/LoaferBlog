package com.loafer.blog.service;

import com.loafer.blog.entity.Role;

import java.util.Map;

public interface RoleService {
    Map<String, Object> getRoles();
    Map<String, Object> createRole(Role role);
    Map<String, Object> updateRole(Long id, Role role);
    Map<String, Object> deleteRole(Long id);
}