# Loafer Blog - 添加删除时间字段和修改唯一约束 Implementation Plan

## [ ] Task 1: 为所有数据库表添加 delete_time 字段
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 修改 `init.sql` 文件，为所有表添加 `delete_time` 字段
  - 字段类型为 TIMESTAMP，默认为 NULL
  - 添加字段注释
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 执行数据库初始化脚本后，所有表都有 `delete_time` 字段
  - `programmatic` TR-1.2: 字段类型为 TIMESTAMP，默认为 NULL
- **Notes**: 确保所有表都添加了该字段，包括但不限于：role, user, user_role, announcement, category, tag, post, post_category, post_tag, comment, message, post_like, post_favorite, post_view_history, sensitive_word

## [ ] Task 2: 修改 mybatis-plus 配置，添加 delete_time 自动填充
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 修改 `MyBatisPlusConfig.java` 文件
  - 在 `metaObjectHandler` 中添加 `delete_time` 字段的自动填充逻辑
  - 确保在逻辑删除时自动填充当前时间
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-2.1: 执行逻辑删除操作后，`delete_time` 字段被自动填充为当前时间
- **Notes**: 需要确保 MyBatis Plus 的逻辑删除配置正确，并且自动填充策略能够正确触发

## [ ] Task 3: 修改具有唯一约束的表的约束条件
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 修改 `init.sql` 文件中具有唯一约束的表的约束条件
  - 将 `deleted` 和 `delete_time` 字段加入唯一约束
  - 涉及的表包括：role, user, user_role, category, tag, post_category, post_tag, post_like, post_favorite, post_view_history, sensitive_word
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 执行数据库初始化脚本后，所有具有唯一约束的表的约束条件都包含了 `deleted` 和 `delete_time` 字段
- **Notes**: 确保唯一约束的修改不会影响现有数据的完整性

## [ ] Task 4: 测试逻辑删除后可以创建相同的记录
- **Priority**: P1
- **Depends On**: Task 1, Task 2, Task 3
- **Description**: 
  - 创建测试用例，验证逻辑删除后可以创建相同的记录
  - 测试具有唯一约束的表，如用户表、角色表等
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-4.1: 逻辑删除一条记录后，创建相同的记录成功，不会触发唯一约束冲突
- **Notes**: 确保测试覆盖所有具有唯一约束的表

## [ ] Task 5: 更新所有实体类，添加 deleteTime 字段
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 更新所有实体类，添加 `deleteTime` 字段
  - 确保字段类型为 `LocalDateTime`
  - 添加 `@TableField` 注解，指定字段名和自动填充策略
- **Acceptance Criteria Addressed**: AC-1, AC-2
- **Test Requirements**:
  - `programmatic` TR-5.1: 所有实体类都有 `deleteTime` 字段
  - `programmatic` TR-5.2: 字段类型为 `LocalDateTime`
  - `programmatic` TR-5.3: 字段有正确的 `@TableField` 注解
- **Notes**: 确保所有实体类都更新，与数据库表结构保持一致