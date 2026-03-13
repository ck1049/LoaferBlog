# 评论功能优化 - 产品需求文档

## Overview
- **Summary**: 优化评论功能，修复循环嵌套数据库查询导致的性能问题，解决安全隐患，并整理项目结构，确保代码符合开发规范。
- **Purpose**: 提高系统性能，增强安全性，优化代码结构，确保代码符合开发规范。
- **Target Users**: 系统开发者和维护者。

## Goals
- 修复循环嵌套数据库查询导致的性能问题
- 解决直接返回User对象带来的安全隐患
- 整理项目结构，确保dto和vo目录在model目录下
- 只保留model.vo.UserVO，移除其他重复的UserVO
- 确保所有接口返回UserVO而非User对象

## Non-Goals (Out of Scope)
- 不修改评论的基本功能逻辑
- 不修改其他业务功能
- 不修改前端代码

## Background & Context
- 当前评论功能存在循环嵌套数据库查询，性能压力大
- 直接返回User对象存在安全隐患
- 项目结构混乱，存在多个dto和vo目录
- 存在多个UserVO类，需要统一使用model.vo.UserVO

## Functional Requirements
- **FR-1**: 修复循环嵌套数据库查询，改为批量查询用户信息
- **FR-2**: 使用UserVO替换User对象作为接口返回值
- **FR-3**: 整理项目结构，确保dto和vo目录在model目录下
- **FR-4**: 只保留model.vo.UserVO，移除其他重复的UserVO

## Non-Functional Requirements
- **NFR-1**: 性能优化，减少数据库查询次数
- **NFR-2**: 安全性增强，避免直接返回User对象
- **NFR-3**: 代码结构清晰，符合开发规范

## Constraints
- **Technical**: 后端使用Spring Boot + MyBatis Plus
- **Dependencies**: 依赖model.vo.UserVO的增强方法

## Assumptions
- model.vo.UserVO已经实现了邮箱自动解密并脱敏、头像地址转URL方法
- 所有需要用户信息的地方都应该使用UserVO而非User对象

## Acceptance Criteria

### AC-1: 性能优化
- **Given**: 有大量评论的帖子
- **When**: 加载评论列表
- **Then**: 数据库查询次数应减少，性能应显著提升
- **Verification**: `programmatic`

### AC-2: 安全增强
- **Given**: 访问评论接口
- **When**: 查看返回数据
- **Then**: 不应包含敏感用户信息，邮箱应脱敏显示
- **Verification**: `programmatic`

### AC-3: 项目结构优化
- **Given**: 查看项目目录结构
- **When**: 检查dto和vo目录
- **Then**: 所有dto和vo应在model目录下，不应有重复的UserVO
- **Verification**: `human-judgment`

## Open Questions
- [ ] 是否需要修改其他使用User对象的地方？
- [ ] 如何处理现有的其他vo目录下的类？