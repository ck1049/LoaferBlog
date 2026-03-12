# Loafer Blog 功能修复 - 产品需求文档

## Overview
- **Summary**: 修复Loafer Blog系统中的多个功能问题，包括文件大小限制配置、邮箱注册和个人资料管理、默认头像生成、消息系统和联系人列表等。
- **Purpose**: 确保系统的所有功能正常运行，提升用户体验。
- **Target Users**: 所有Loafer Blog用户，包括普通用户和管理员。

## Goals
- 修复文件大小限制配置，支持三个不同的配置项，并确保容器文件解析配置正确更新
- 修复邮箱注册功能，取消前端必填限制，调整数据库字段长度
- 实现个人资料页面邮箱回显功能
- 修复联系人列表的头像显示和用户名称显示问题
- 为新注册用户生成默认头像并更新user表
- 修复消息相关接口的返回参数和日期处理问题，添加朋友功能入口

## Non-Goals (Out of Scope)
- 不修改系统的核心架构
- 不改变现有的技术栈
- 不添加新的第三方依赖

## Background & Context
- Loafer Blog是一个现有的博客系统，使用Spring Boot + MyBatis Plus + PostgreSQL
- 系统已实现基本的用户管理、文章发布、消息等功能
- 前端使用Vue.js框架

## Functional Requirements
- **FR-1**: 文件大小限制配置
  - 支持三个不同的配置项：图片文件限制、视频文件限制、其他文件限制
  - 修改逻辑不仅更新数据库表，还需要更新容器文件解析的配置
  - 配置MultipartConfigElement从数据库中读取配置，而不是硬编码

- **FR-2**: 邮箱注册功能
  - 取消前端注册表单的邮箱必填限制
  - 调整数据库user表的email字段长度，确保能存储加密后的密文

- **FR-3**: 个人资料页面
  - 增加邮箱的回显功能，显示脱敏后的邮箱地址

- **FR-4**: 联系人列表
  - 为联系人列表中的用户设置正确的头像src属性
  - 优先显示用户的昵称，昵称为空则显示用户名
  - 修复管理员用户显示为"未知用户"的问题

- **FR-5**: 默认头像生成
  - 为新注册的用户生成默认头像
  - 更新user表的头像信息

- **FR-6**: 消息系统
  - 修复消息相关接口的返回参数，避免使用Map作为返回参数
  - 修复LocalDateTime字段强转Date字段的错误
  - 添加朋友功能入口

## Non-Functional Requirements
- **NFR-1**: 系统稳定性，所有功能正常运行
- **NFR-2**: 代码质量，遵循Java代码规范
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

### AC-1: 文件大小限制配置
- **Given**: 管理员进入文件大小限制配置页面
- **When**: 修改图片、视频和其他文件的大小限制
- **Then**: 配置被正确保存到数据库，且系统的文件上传限制被更新
- **Verification**: `programmatic`

### AC-2: 邮箱注册功能
- **Given**: 用户注册新账号
- **When**: 不填写邮箱字段
- **Then**: 注册成功，邮箱字段为空
- **Verification**: `programmatic`

### AC-3: 个人资料页面
- **Given**: 用户进入个人资料页面
- **When**: 查看个人信息
- **Then**: 邮箱以脱敏形式显示
- **Verification**: `human-judgment`

### AC-4: 联系人列表
- **Given**: 用户进入消息页面
- **When**: 查看联系人列表
- **Then**: 联系人头像显示正确，用户名称优先显示昵称
- **Verification**: `human-judgment`

### AC-5: 默认头像生成
- **Given**: 新用户注册
- **When**: 注册成功
- **Then**: 系统为用户生成默认头像并更新到user表
- **Verification**: `programmatic`

### AC-6: 消息系统
- **Given**: 用户使用消息功能
- **When**: 查看联系人列表和聊天记录
- **Then**: 消息显示正常，日期格式正确，添加朋友功能可访问
- **Verification**: `human-judgment`

## Open Questions
- [ ] 前端文件大小限制配置页面的具体实现
- [ ] 前端注册表单的邮箱字段修改
- [ ] 前端个人资料页面的邮箱回显实现
- [ ] 前端联系人列表的头像和名称显示逻辑