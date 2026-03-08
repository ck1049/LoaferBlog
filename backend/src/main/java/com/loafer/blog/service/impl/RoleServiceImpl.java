package com.loafer.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.loafer.blog.model.dto.RoleDTO;
import com.loafer.blog.model.entity.Role;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.RoleVO;
import com.loafer.blog.mapper.RoleMapper;
import com.loafer.blog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public ResponseVO<List<RoleVO>> getRoles() {
        try {
            List<Role> roles = roleMapper.selectList(null);
            List<RoleVO> roleVOs = roles.stream().map(RoleVO::new).collect(Collectors.toList());
            return ResponseVO.success(roleVOs);
        } catch (Exception e) {
            return ResponseVO.error("获取角色列表失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<RoleVO> createRole(RoleDTO roleDTO) {
        try {
            // 检查角色名是否已存在
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.eq("name", roleDTO.getName());
            if (roleMapper.selectOne(wrapper) != null) {
                return ResponseVO.error("角色名已存在");
            }

            Role role = new Role();
            role.setName(roleDTO.getName());
            role.setDescription(roleDTO.getDescription());
            role.setCreateTime(LocalDateTime.now());
            role.setUpdateTime(LocalDateTime.now());
            roleMapper.insert(role);
            
            RoleVO roleVO = new RoleVO(role);
            return ResponseVO.success(roleVO);
        } catch (Exception e) {
            return ResponseVO.error("创建角色失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<RoleVO> updateRole(Long id, RoleDTO roleDTO) {
        try {
            Role existingRole = roleMapper.selectById(id);
            if (existingRole == null) {
                return ResponseVO.error("角色不存在");
            }

            // 检查角色名是否已存在
            QueryWrapper<Role> wrapper = new QueryWrapper<>();
            wrapper.eq("name", roleDTO.getName());
            wrapper.ne("id", id);
            if (roleMapper.selectOne(wrapper) != null) {
                return ResponseVO.error("角色名已存在");
            }

            existingRole.setName(roleDTO.getName());
            existingRole.setDescription(roleDTO.getDescription());
            existingRole.setUpdateTime(LocalDateTime.now());
            roleMapper.updateById(existingRole);
            
            RoleVO roleVO = new RoleVO(existingRole);
            return ResponseVO.success(roleVO);
        } catch (Exception e) {
            return ResponseVO.error("更新角色失败: " + e.getMessage());
        }
    }

    @Override
    public ResponseVO<Void> deleteRole(Long id) {
        try {
            Role existingRole = roleMapper.selectById(id);
            if (existingRole == null) {
                return ResponseVO.error("角色不存在");
            }

            roleMapper.deleteById(id);
            return ResponseVO.success(null);
        } catch (Exception e) {
            return ResponseVO.error("删除角色失败: " + e.getMessage());
        }
    }
}