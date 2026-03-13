# JWT 角色认证安全修复 - 产品需求文档

## Overview
- **Summary**: 修复 JWT 认证中的角色分配问题，确保生成 JWT 时将角色列表写入 payload，JwtAuthenticationFilter 解析 JWT 时从 payload 中提取用户 ID 和角色列表，而不是硬编码角色。
- **Purpose**: 提高系统安全性，确保用户只能获得其实际拥有的角色权限。
- **Target Users**: 系统管理员和普通用户。

## Goals
- 修改 JwtUtils 类，在生成 JWT 时将角色列表写入 payload
- 修改 JwtAuthenticationFilter 类，从 JWT payload 中解析角色列表，不再硬编码角色
- 确保角色权限验证正常工作

## Non-Goals (Out of Scope)
- 不修改现有的角色管理功能
- 不修改现有的权限控制逻辑
- 不修改用户注册和登录流程

## Background & Context
- 当前 JwtAuthenticationFilter 中硬编码了所有用户都拥有 "ROLE_USER" 和 "ROLE_ADMIN" 角色，存在严重的安全问题
- 生成 JWT 时只写入了用户 ID，没有写入角色列表
- 系统已经有完整的角色管理功能，包括用户-角色关联表

## Functional Requirements
- **FR-1**: 修改 JwtUtils 类，在生成 JWT 时支持写入角色列表到 payload
- **FR-2**: 修改 JwtAuthenticationFilter 类，从 JWT payload 中解析角色列表
- **FR-3**: 确保角色权限验证正常工作，用户只能获得其实际拥有的角色

## Non-Functional Requirements
- **NFR-1**: 修复后的认证系统应保持高性能
- **NFR-2**: 修复后的认证系统应保持兼容性，不影响现有功能
- **NFR-3**: 修复后的认证系统应符合安全最佳实践

## Constraints
- **Technical**: 后端使用 Spring Boot + Spring Security + JWT
- **Dependencies**: 依赖现有的角色管理功能

## Assumptions
- 系统已经有完整的角色管理功能
- 系统已经有用户-角色关联表

## Acceptance Criteria

### AC-1: JWT 生成时包含角色列表
- **Given**: 用户登录
- **When**: 系统生成 JWT
- **Then**: JWT payload 中应包含用户的角色列表
- **Verification**: `programmatic`

### AC-2: JWT 解析时提取角色列表
- **Given**: 用户发送带 JWT 的请求
- **When**: JwtAuthenticationFilter 解析 JWT
- **Then**: 应从 JWT payload 中提取用户 ID 和角色列表
- **Verification**: `programmatic`

### AC-3: 角色权限验证正常
- **Given**: 用户发送请求
- **When**: 系统进行权限验证
- **Then**: 用户只能访问其角色允许的资源
- **Verification**: `human-judgment`

## Open Questions
- [ ] 现有的角色名称是否需要添加 "ROLE_" 前缀？