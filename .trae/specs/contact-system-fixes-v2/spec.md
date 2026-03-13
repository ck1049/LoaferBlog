# 联系人系统修复 - 产品需求文档

## Overview
- **Summary**: 修复联系人系统的消息显示问题和布局美观问题，包括最近一条消息不展示、红点显示不全以及布局美观度下降的问题。
- **Purpose**: 解决用户反馈的紧急问题，提高联系人系统的用户体验和美观度。
- **Target Users**: 所有注册用户，包括普通用户和管理员。

## Goals
- 修复联系人列表最近一条消息不展示的问题
- 修复红点显示不全的问题
- 恢复并优化布局美观度

## Non-Goals (Out of Scope)
- 不修改其他功能模块
- 不添加新的功能
- 不改变现有的数据结构

## Background & Context
- 联系人列表最近一条消息不展示，影响用户体验
- 红点位置正确但被截断，显示不全
- 之前的布局修改导致美观度下降

## Functional Requirements
- **FR-1**: 修复联系人列表最近一条消息不展示的问题
- **FR-2**: 修复红点显示不全的问题
- **FR-3**: 优化布局美观度

## Non-Functional Requirements
- **NFR-1**: 页面响应速度快，加载时间不超过1秒
- **NFR-2**: 界面设计保持一致性，符合现有系统风格
- **NFR-3**: 移动端布局适配不同分辨率屏幕

## Constraints
- **Technical**: 基于现有Vue 3 + TypeScript技术栈
- **Business**: 保持现有功能的兼容性，不破坏已有功能

## Assumptions
- 现有数据库结构和API接口保持不变
- 用户已登录并具有有效的认证状态
- 消息系统的基本功能正常运行

## Acceptance Criteria

### AC-1: 联系人列表消息显示
- **Given**: 用户与联系人有消息往来
- **When**: 用户查看联系人列表
- **Then**: 最近一条消息正常显示
- **Verification**: `human-judgment`

### AC-2: 红点显示完整
- **Given**: 用户收到新消息
- **When**: 用户查看联系人列表
- **Then**: 红点完整显示，不被截断
- **Verification**: `human-judgment`

### AC-3: 布局美观度
- **Given**: 用户访问系统
- **When**: 用户查看任何页面
- **Then**: 布局美观，左右留白平衡，导航栏显示正常
- **Verification**: `human-judgment`

## Open Questions
- [ ] 具体的布局美观度标准
- [ ] 消息不显示的具体原因