package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.model.entity.Role;
import com.loafer.blog.mapper.RoleMapper;
import com.loafer.blog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Map<String, Object> getRoles() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Role> roles = roleMapper.selectList(null);
            result.put("code", 200);
            result.put("message", "获取角色列表成功");
            result.put("data", roles);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取角色列表失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> createRole(Role role) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 检查角色名是否已存在
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.eq("name", role.getName());
            if (roleMapper.selectOne(wrapper) != null) {
                result.put("code", 400);
                result.put("message", "角色名已存在");
                return result;
            }

            roleMapper.insert(role);
            result.put("code", 200);
            result.put("message", "创建角色成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "创建角色失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> updateRole(Long id, Role role) {
        Map<String, Object> result = new HashMap<>();
        try {
            Role existingRole = roleMapper.selectById(id);
            if (existingRole == null) {
                result.put("code", 400);
                result.put("message", "角色不存在");
                return result;
            }

            // 检查角色名是否已存在
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.eq("name", role.getName());
            wrapper.ne("id", id);
            if (roleMapper.selectOne(wrapper) != null) {
                result.put("code", 400);
                result.put("message", "角色名已存在");
                return result;
            }

            role.setId(id);
            roleMapper.updateById(role);
            result.put("code", 200);
            result.put("message", "更新角色成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "更新角色失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> deleteRole(Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            Role existingRole = roleMapper.selectById(id);
            if (existingRole == null) {
                result.put("code", 400);
                result.put("message", "角色不存在");
                return result;
            }

            roleMapper.deleteById(id);
            result.put("code", 200);
            result.put("message", "删除角色成功");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "删除角色失败: " + e.getMessage());
        }
        return result;
    }
}