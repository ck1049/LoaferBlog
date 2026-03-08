package com.loafer.blog.controller;

import com.loafer.blog.model.dto.RoleDTO;
import com.loafer.blog.model.vo.ResponseVO;
import com.loafer.blog.model.vo.RoleVO;
import com.loafer.blog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public ResponseVO<List<RoleVO>> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping
    public ResponseVO<RoleVO> createRole(@RequestBody RoleDTO roleDTO) {
        return roleService.createRole(roleDTO);
    }

    @PutMapping("/{id}")
    public ResponseVO<RoleVO> updateRole(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        return roleService.updateRole(id, roleDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseVO<Void> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }
}