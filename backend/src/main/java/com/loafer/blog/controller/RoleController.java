package com.loafer.blog.controller;

import com.loafer.blog.model.entity.Role;
import com.loafer.blog.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public Map<String, Object> getRoles() {
        return roleService.getRoles();
    }

    @PostMapping
    public Map<String, Object> createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateRole(@PathVariable Long id, @RequestBody Role role) {
        return roleService.updateRole(id, role);
    }

    @DeleteMapping("/{id}")
    public Map<String, Object> deleteRole(@PathVariable Long id) {
        return roleService.deleteRole(id);
    }
}