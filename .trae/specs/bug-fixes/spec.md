# Loafer Blog Bug 修复 - 产品需求文档

## Overview
- **Summary**: 修复Loafer Blog系统中的多个bug，包括文件大小过滤器、联系人列表、聊天消息显示、文件上传按钮等问题。
- **Purpose**: 确保系统的所有功能正常运行，提升用户体验。
- **Target Users**: 所有Loafer Blog用户，包括普通用户和管理员。

## Goals
- 修复文件大小过滤器，区分文件类型并优化性能
- 确保邮箱注册表单的必填限制已被正确删除
- 处理UserSettingsView.vue和UserView.vue的关系，将邮箱相关逻辑挪到UserView.vue
- 修复联系人列表显示问题，确保正确显示用户昵称、用户名和头像
- 修复聊天消息显示问题，确保日期格式正确
- 修复上传文件按钮的功能

## Non-Goals (Out of Scope)
- 不修改系统的核心架构
- 不改变现有的技术栈
- 不添加新的第三方依赖

## Background & Context
- Loafer Blog是一个现有的博客系统，使用Spring Boot + MyBatis Plus + PostgreSQL
- 系统已实现基本的用户管理、文章发布、消息等功能
- 前端使用Vue.js框架

## Functional Requirements
- **FR-1**: 文件大小过滤器修复
  - 区分文件类型，根据不同类型使用不同的大小限制
  - 优化性能，避免每次都查询数据库
  - 确保过滤器能正确检查文件大小

- **FR-2**: 邮箱注册表单
  - 确保邮箱字段的必填限制已被删除

- **FR-3**: 用户中心页面
  - 删除UserSettingsView.vue
  - 将邮箱相关逻辑挪到UserView.vue

- **FR-4**: 联系人列表
  - 确保正确显示用户昵称和用户名
  - 确保正确显示用户头像，包括拼接域名前缀
  - 修复"未知用户"的显示问题

- **FR-5**: 聊天消息
  - 确保消息能正常显示
  - 确保日期格式正确

- **FR-6**: 文件上传按钮
  - 修复上传文件按钮的功能，确保点击后能正常工作

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

### AC-1: 文件大小过滤器
- **Given**: 用户上传不同类型的文件
- **When**: 上传文件时
- **Then**: 系统根据文件类型使用不同的大小限制，且性能良好
- **Verification**: `programmatic`

### AC-2: 邮箱注册表单
- **Given**: 用户注册新账号
- **When**: 不填写邮箱字段
- **Then**: 注册成功，邮箱字段为空
- **Verification**: `human-judgment`

### AC-3: 用户中心页面
- **Given**: 用户进入用户中心
- **When**: 查看个人信息
- **Then**: 页面显示邮箱信息，且UserSettingsView.vue已被删除
- **Verification**: `human-judgment`

### AC-4: 联系人列表
- **Given**: 用户进入消息页面
- **When**: 查看联系人列表
- **Then**: 联系人显示正确的昵称、用户名和头像
- **Verification**: `human-judgment`

### AC-5: 聊天消息
- **Given**: 用户查看聊天记录
- **When**: 加载聊天消息
- **Then**: 消息显示正常，日期格式正确
- **Verification**: `human-judgment`

### AC-6: 文件上传按钮
- **Given**: 用户点击上传文件按钮
- **When**: 选择文件后
- **Then**: 文件上传功能正常工作
- **Verification**: `human-judgment`

## Open Questions
- [ ] UserSettingsView.vue的具体位置
- [ ] UserView.vue的具体内容和结构
- [ ] 前端联系人列表组件的具体实现
- [ ] 前端聊天消息组件的具体实现
- [ ] 前端文件上传按钮的具体实现