# Loafer Blog - 添加删除时间字段和修改唯一约束 Product Requirement Document

## Overview
- **Summary**: 为 Loafer Blog 项目的所有数据库表添加 `delete_time` 字段，并修改 mybatis-plus 配置以支持自动填充，同时更新具有唯一约束的表的约束条件，确保逻辑删除后的数据可以重新创建。
- **Purpose**: 提高数据管理的完整性和灵活性，确保逻辑删除的数据不会影响新数据的创建。
- **Target Users**: 系统开发人员和数据库管理员。

## Goals
- 为所有数据库表添加 `delete_time` 字段，默认为空
- 在 mybatis-plus 配置类中设置 `delete_time` 字段的自动填充策略
- 修改所有具有唯一约束的表的约束条件，将 `deleted` 和 `delete_time` 字段加入唯一约束

## Non-Goals (Out of Scope)
- 修改现有的业务逻辑
- 改变现有的数据结构
- 影响现有的 API 接口

## Background & Context
- 目前系统使用 `deleted` 字段进行逻辑删除，但缺少 `delete_time` 字段来记录删除时间
- 对于具有唯一约束的表，当数据被逻辑删除后，无法创建相同的记录，因为唯一约束仍然生效
- 添加 `delete_time` 字段并修改唯一约束可以解决这个问题，同时提供更完整的删除记录

## Functional Requirements
- **FR-1**: 为所有数据库表添加 `delete_time` 字段，类型为 TIMESTAMP，默认为 NULL
- **FR-2**: 在 mybatis-plus 配置类中设置 `delete_time` 字段的自动填充策略，在逻辑删除时自动填充当前时间
- **FR-3**: 修改所有具有唯一约束的表的约束条件，将 `deleted` 和 `delete_time` 字段加入唯一约束

## Non-Functional Requirements
- **NFR-1**: 所有修改必须保持向后兼容，不影响现有数据
- **NFR-2**: 数据库迁移必须安全，确保数据不丢失
- **NFR-3**: 代码修改必须遵循现有的编码规范

## Constraints
- **Technical**: 使用 PostgreSQL 数据库，MyBatis Plus 框架
- **Business**: 无特殊业务约束
- **Dependencies**: 无外部依赖

## Assumptions
- 系统使用逻辑删除而非物理删除
- 所有表都有 `deleted` 字段用于逻辑删除标记
- MyBatis Plus 已经配置了逻辑删除功能

## Acceptance Criteria

### AC-1: 所有表都添加了 delete_time 字段
- **Given**: 数据库初始化脚本执行后
- **When**: 检查表结构
- **Then**: 所有表都有 `delete_time` 字段，类型为 TIMESTAMP，默认为 NULL
- **Verification**: `programmatic`

### AC-2: mybatis-plus 配置了 delete_time 自动填充
- **Given**: 应用启动后
- **When**: 执行逻辑删除操作
- **Then**: `delete_time` 字段被自动填充为当前时间
- **Verification**: `programmatic`

### AC-3: 具有唯一约束的表的约束条件已更新
- **Given**: 数据库初始化脚本执行后
- **When**: 检查表的唯一约束
- **Then**: 所有具有唯一约束的表的约束条件都包含了 `deleted` 和 `delete_time` 字段
- **Verification**: `programmatic`

### AC-4: 逻辑删除后可以创建相同的记录
- **Given**: 一条记录被逻辑删除
- **When**: 尝试创建相同的记录
- **Then**: 创建成功，不会触发唯一约束冲突
- **Verification**: `programmatic`

## Open Questions
- [ ] 无