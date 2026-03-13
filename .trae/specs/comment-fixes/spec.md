# 评论功能修复 - 产品需求文档

## Overview
- **Summary**: 修复帖子详情页评论功能存在的问题，包括时间显示异常、回复数和展开按钮不显示、按钮样式优化等。
- **Purpose**: 确保评论功能正常工作，提升用户体验。
- **Target Users**: 所有使用博客系统的用户。

## Goals
- 修复评论时间显示异常问题（NaN-NaN-NaN NaN:NaN）
- 确保有回复的评论显示回复数和展开按钮
- 优化展开/折叠按钮的样式
- 确保评论的嵌套渲染正常工作

## Non-Goals (Out of Scope)
- 不修改评论的发布和删除功能
- 不修改评论的分页加载逻辑
- 不修改其他页面的评论展示

## Background & Context
- 现有评论功能已经实现了基本的评论和回复功能
- 后端已经实现了评论的分页查询和回复查询接口
- 前端已经有基本的评论展示界面，但存在时间显示异常、回复数和展开按钮不显示等问题

## Functional Requirements
- **FR-1**: 修复评论时间显示异常问题，确保时间格式正确
- **FR-2**: 确保有回复的评论显示回复数和展开按钮
- **FR-3**: 优化展开/折叠按钮的样式，使其更加美观
- **FR-4**: 确保点击展开按钮后能正确加载和渲染回复

## Non-Functional Requirements
- **NFR-1**: 回复的加载应平滑，避免页面卡顿
- **NFR-2**: 按钮样式应美观，与整体UI风格一致
- **NFR-3**: 响应式设计，在不同设备上都能正常显示

## Constraints
- **Technical**: 前端使用Vue 3 + Pinia，后端使用Spring Boot + MyBatis Plus
- **Dependencies**: 依赖后端的评论查询接口和用户信息

## Assumptions
- 后端API能够返回评论相关的用户信息（包括头像）
- 前端能够正确处理回复的加载和渲染

## Acceptance Criteria

### AC-1: 评论时间显示正常
- **Given**: 用户访问帖子详情页
- **When**: 页面加载完成
- **Then**: 每条评论的时间应显示为正确的格式，不出现NaN
- **Verification**: `human-judgment`

### AC-2: 回复数和展开按钮显示
- **Given**: 评论有回复
- **When**: 评论加载完成
- **Then**: 评论下方应显示回复数和展开按钮
- **Verification**: `human-judgment`

### AC-3: 展开/折叠功能正常
- **Given**: 评论有回复
- **When**: 用户点击展开按钮
- **Then**: 应加载并显示该评论的回复
- **When**: 用户点击折叠按钮
- **Then**: 应隐藏已加载的回复
- **Verification**: `human-judgment`

### AC-4: 按钮样式美观
- **Given**: 用户访问帖子详情页
- **When**: 查看评论区域
- **Then**: 展开/折叠按钮应美观，与整体UI风格一致
- **Verification**: `human-judgment`

## Open Questions
- [ ] 后端API是否返回了评论的回复数？
- [ ] 前端是否需要在评论加载时就查询回复数？