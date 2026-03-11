# LoaferBlog 功能增强 - 产品需求文档

## Overview
- **Summary**: 对LoaferBlog进行功能增强，包括用户消息系统优化、管理功能修复、安全问题修复和用户体验改进。
- **Purpose**: 提升用户体验，修复现有bug，增强系统安全性。
- **Target Users**: 所有LoaferBlog用户，包括普通用户和管理员。

## Goals
- 实现新用户注册后管理员自动发送欢迎消息的功能
- 优化消息页面，增加用户搜索和聊天功能
- 修复管理员管理按钮的bug
- 修复评论和分类查询的403错误
- 实现邮箱脱敏展示和修改功能
- 实现失效token校验功能

## Non-Goals (Out of Scope)
- 不涉及前端UI的整体重构
- 不修改现有的核心数据结构
- 不添加新的第三方依赖

## Background & Context
- LoaferBlog是一个现有的博客系统，包含前端和后端两部分
- 前端使用Vue 3 + TypeScript
- 后端使用Java + Spring Boot
- 系统已有的功能包括用户注册、登录、发布文章、评论、消息等

## Functional Requirements
- **FR-1**: 新用户注册后，管理员自动向用户发送欢迎消息："Hi，欢迎加入LoaferBlog。"
- **FR-2**: 消息页面增加根据用户名（可为空）搜索用户列表的功能，支持上滑翻页
- **FR-3**: 消息页面用户列表需要包含用户头像、昵称(用户名)、个性签名，排版美观
- **FR-4**: 点击消息页面的用户卡片自动打开双人聊天窗口页，参考微信聊天功能
- **FR-5**: 消息页同一组联系人（不区分方向）只能出现一个消息卡片
- **FR-6**: 修复管理员的"管理"按钮bug，使其能够正常打开管理页面
- **FR-7**: 修复提交评论报403的问题
- **FR-8**: 修复分类查询报403的问题
- **FR-9**: 个人资料页增加邮箱的脱敏展示和修改功能
- **FR-10**: 登录接口返回的用户信息应对邮箱做脱敏处理
- **FR-11**: 实现失效token校验功能，后端维护退出登录token缓存，有效期和token生命周期一致

## Non-Functional Requirements
- **NFR-1**: 消息系统的响应时间应在1秒以内
- **NFR-2**: 邮箱脱敏处理应符合数据安全标准
- **NFR-3**: 系统应保持现有性能水平，不因为新功能而降低
- **NFR-4**: 新功能应与现有系统无缝集成

## Constraints
- **Technical**: 基于现有技术栈，不引入新的框架或库
- **Business**: 功能实现应符合现有系统的设计理念
- **Dependencies**: 依赖现有的用户系统和消息系统

## Assumptions
- 系统已有管理员账户，能够向新用户发送消息
- 系统已有消息存储和展示功能
- 系统已有用户认证和授权机制

## Acceptance Criteria

### AC-1: 新用户注册后自动发送欢迎消息
- **Given**: 新用户完成注册
- **When**: 注册成功后
- **Then**: 系统自动向新用户发送欢迎消息："Hi，欢迎加入LoaferBlog。"
- **Verification**: `programmatic`
- **Notes**: 消息应由管理员账户发送

### AC-2: 消息页面用户搜索功能
- **Given**: 用户进入消息页面
- **When**: 在搜索框输入用户名并点击搜索
- **Then**: 系统显示匹配的用户列表，支持上滑翻页
- **Verification**: `programmatic`
- **Notes**: 搜索框为空时应显示所有用户

### AC-3: 消息页面用户列表展示
- **Given**: 用户进入消息页面
- **When**: 系统加载用户列表
- **Then**: 列表中的每个用户卡片包含头像、昵称(用户名)、个性签名，排版美观
- **Verification**: `human-judgment`

### AC-4: 点击用户卡片打开聊天窗口
- **Given**: 用户在消息页面
- **When**: 点击某个用户卡片
- **Then**: 系统自动打开与该用户的双人聊天窗口
- **Verification**: `programmatic`
- **Notes**: 聊天窗口应参考微信聊天功能的设计

### AC-5: 消息卡片去重
- **Given**: 用户在消息页面
- **When**: 查看消息列表
- **Then**: 同一组联系人（不区分方向）只出现一个消息卡片
- **Verification**: `programmatic`

### AC-6: 修复管理员管理按钮
- **Given**: 管理员登录系统
- **When**: 点击"管理"按钮
- **Then**: 系统正常打开管理页面，无错误
- **Verification**: `programmatic`
- **Notes**: 修复vue-router相关的错误

### AC-7: 修复评论403错误
- **Given**: 用户登录系统
- **When**: 提交评论
- **Then**: 评论成功提交，无403错误
- **Verification**: `programmatic`

### AC-8: 修复分类查询403错误
- **Given**: 用户访问系统
- **When**: 查询分类列表
- **Then**: 分类列表成功加载，无403错误
- **Verification**: `programmatic`

### AC-9: 个人资料页邮箱脱敏展示和修改
- **Given**: 用户进入个人资料页面
- **When**: 查看个人信息
- **Then**: 邮箱以脱敏形式展示（如：ex****@example.com）
- **Verification**: `human-judgment`

### AC-10: 个人资料页邮箱修改
- **Given**: 用户进入个人资料页面并点击修改邮箱
- **When**: 输入新邮箱并提交
- **Then**: 系统验证邮箱格式，如为脱敏格式则不更新
- **Verification**: `programmatic`

### AC-11: 登录接口邮箱脱敏
- **Given**: 用户登录系统
- **When**: 登录成功
- **Then**: 接口返回的用户信息中邮箱为脱敏格式
- **Verification**: `programmatic`

### AC-12: 失效token校验
- **Given**: 用户退出登录
- **When**: 使用已退出的token访问系统
- **Then**: 系统检测到token已失效，强制跳转到登录页
- **Verification**: `programmatic`

## Open Questions
- [ ] 管理员账户的ID或标识是什么？
- [ ] 消息系统的存储结构是怎样的？
- [ ] 邮箱脱敏的具体规则是什么？
- [ ] token的有效期是多长时间？