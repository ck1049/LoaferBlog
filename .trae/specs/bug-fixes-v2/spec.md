# Loafer Blog Bug 修复 v2 - 产品需求文档

## Overview
- **Summary**: 修复Loafer Blog系统中的多个bug，包括还原UserSettingsView.vue、优化文件大小限制查询逻辑、修复聊天消息渲染问题和改进文件类型判断逻辑。
- **Purpose**: 确保系统的所有功能正常运行，提升用户体验。
- **Target Users**: 所有Loafer Blog用户，包括普通用户和管理员。

## Goals
- 还原UserSettingsView.vue文件
- 优化文件大小限制的查询逻辑，使用全局缓存
- 修复聊天消息没有渲染的问题
- 改进FileSizeLimitFilter的文件类型判断逻辑

## Non-Goals (Out of Scope)
- 不修改系统的核心架构
- 不改变现有的技术栈
- 不添加新的第三方依赖

## Background & Context
- Loafer Blog是一个现有的博客系统，使用Spring Boot + MyBatis Plus + PostgreSQL
- 系统已实现基本的用户管理、文章发布、消息等功能
- 前端使用Vue.js框架

## Functional Requirements
- **FR-1**: 还原UserSettingsView.vue
  - 重新创建UserSettingsView.vue文件
  - 确保其包含邮箱修改等功能

- **FR-2**: 优化文件大小限制查询逻辑
  - 实现全局缓存机制
  - FileSizeLimitFilter从全局缓存读取配置
  - 配置更新时清空缓存

- **FR-3**: 修复聊天消息渲染问题
  - 确保聊天消息能正确渲染
  - 检查消息数据结构和前端渲染逻辑

- **FR-4**: 改进文件类型判断逻辑
  - 修改FileSizeLimitFilter.getMaxFileSizeByType方法
  - 通过文件类型而不是URI判断
  - 确保能正确识别不同类型的文件

## Non-Functional Requirements
- **NFR-1**: 系统稳定性，所有功能正常运行
- **NFR-2**: 性能优化，避免不必要的数据库查询
- **NFR-3**: 用户体验，界面美观，功能易用

## Constraints
- **Technical**: 基于现有技术栈进行开发
- **Business**: 保持系统稳定性，不影响现有功能
- **Dependencies**: 依赖现有的系统架构和组件

## Assumptions
- 现有系统架构和数据库结构基本合理
- 服务器环境正常，网络连接稳定
- 前端和后端代码结构清晰，便于修改

## Acceptance Criteria

### AC-1: 还原UserSettingsView.vue
- **Given**: 用户访问个人设置页面
- **When**: 进入UserSettingsView.vue
- **Then**: 页面能正常显示，包含邮箱修改功能
- **Verification**: `human-judgment`

### AC-2: 优化文件大小限制查询逻辑
- **Given**: 系统运行时
- **When**: 上传文件时
- **Then**: FileSizeLimitFilter从全局缓存读取配置，配置更新时缓存被清空
- **Verification**: `programmatic`

### AC-3: 修复聊天消息渲染问题
- **Given**: 用户进入聊天窗口
- **When**: 查看聊天记录
- **Then**: 聊天消息能正确渲染，显示消息内容和发送时间
- **Verification**: `human-judgment`

### AC-4: 改进文件类型判断逻辑
- **Given**: 用户上传不同类型的文件
- **When**: 上传文件时
- **Then**: FileSizeLimitFilter能根据文件类型使用正确的大小限制
- **Verification**: `programmatic`

## Open Questions
- [ ] UserSettingsView.vue的具体内容
- [ ] 全局缓存的实现方式
- [ ] 聊天消息渲染问题的具体原因
- [ ] 文件类型判断的具体实现方法
