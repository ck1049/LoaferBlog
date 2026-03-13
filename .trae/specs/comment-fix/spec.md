# 评论功能修复 - 产品需求文档

## Overview
- **Summary**: 修复帖子详情页的评论列表展示问题，删除后端中用不到的评论查询接口，优先使用分页查询接口以避免评论数过多造成的资源浪费和内存溢出问题。
- **Purpose**: 确保评论功能正常展示，提高系统性能和稳定性。
- **Target Users**: 所有使用博客系统的用户。

## Goals
- 修复帖子详情页评论列表不展示的问题
- 删除后端中用不到的`getCommentsByPostId`接口
- 确保前端使用分页查询接口获取评论，避免性能问题

## Non-Goals (Out of Scope)
- 不修改评论的其他功能（如评论发布、删除等）
- 不修改评论的UI设计
- 不修改后端其他接口

## Background & Context
- 后端已经实现了评论的分页查询接口`getCommentsByPostIdWithPagination`
- 前端已经有调用分页接口的代码，但可能存在问题导致评论列表不展示
- 后端存在一个未使用的`getCommentsByPostId`接口，该接口会返回所有评论，可能导致内存溢出

## Functional Requirements
- **FR-1**: 帖子详情页应正确展示评论列表
- **FR-2**: 评论列表应支持分页加载
- **FR-3**: 后端应删除未使用的`getCommentsByPostId`接口

## Non-Functional Requirements
- **NFR-1**: 系统应能处理大量评论而不出现性能问题
- **NFR-2**: 评论加载应快速响应

## Constraints
- **Technical**: 前端使用Vue 3 + Pinia，后端使用Spring Boot + MyBatis Plus
- **Dependencies**: 依赖后端的分页查询接口

## Assumptions
- 后端的分页查询接口功能正常
- 前端代码结构完整，只需要修复评论展示逻辑

## Acceptance Criteria

### AC-1: 评论列表正常展示
- **Given**: 用户访问帖子详情页
- **When**: 页面加载完成
- **Then**: 评论列表应正确显示，包含评论内容、作者、时间等信息
- **Verification**: `human-judgment`

### AC-2: 支持分页加载
- **Given**: 帖子有超过5条评论
- **When**: 用户点击"查看更多"按钮
- **Then**: 应加载更多评论，且页面正常显示
- **Verification**: `human-judgment`

### AC-3: 后端接口优化
- **Given**: 后端代码已修改
- **When**: 调用评论相关接口
- **Then**: 只有分页查询接口可用，原有的`getCommentsByPostId`接口已删除
- **Verification**: `programmatic`

### AC-4: 性能测试
- **Given**: 帖子有大量评论（如1000条）
- **When**: 用户访问帖子详情页
- **Then**: 页面应正常加载，不会出现内存溢出或性能问题
- **Verification**: `programmatic`

## Open Questions
- [ ] 前端是否需要对评论数据进行特殊处理？
- [ ] 后端删除接口后是否需要更新API文档？