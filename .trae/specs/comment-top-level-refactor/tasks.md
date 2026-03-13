# 评论系统重构 - 实现计划

## [ ] 任务 1: 修改评论实体类，增加顶级评论ID字段
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 在Comment实体类中增加topLevelId字段，用于记录评论所属的顶级评论ID
  - 顶级评论的topLevelId为0，子评论的topLevelId为其所属的顶级评论的ID
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 检查Comment实体类是否成功添加topLevelId字段
  - `programmatic` TR-1.2: 检查数据库表是否成功添加topLevelId字段
- **Notes**: 需要同时修改数据库表结构，添加topLevelId字段

## [ ] 任务 2: 修改评论服务，实现按顶级评论ID查询评论的功能
- **Priority**: P0
- **Depends On**: 任务 1
- **Description**:
  - 修改CommentService接口，添加按顶级评论ID查询评论的方法
  - 实现CommentServiceImpl，实现按顶级评论ID查询评论的逻辑
  - 支持分页查询，按时间倒序排列
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-2.1: 检查按顶级评论ID查询评论的方法是否正确实现
  - `programmatic` TR-2.2: 检查分页查询是否正常工作
  - `programmatic` TR-2.3: 检查查询结果是否按时间倒序排列
- **Notes**: 需要修改现有的评论查询逻辑，支持按顶级评论ID查询

## [ ] 任务 3: 修改前端评论存储，支持顶级评论ID
- **Priority**: P0
- **Depends On**: 任务 1
- **Description**:
  - 修改前端的评论存储，添加topLevelId字段
  - 修改评论创建逻辑，正确设置topLevelId
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-3.1: 检查前端评论存储是否成功添加topLevelId字段
  - `programmatic` TR-3.2: 检查评论创建时是否正确设置topLevelId
- **Notes**: 需要确保前端和后端的数据结构保持一致

## [ ] 任务 4: 修改前端评论渲染逻辑，避免无限嵌套
- **Priority**: P0
- **Depends On**: 任务 2, 任务 3
- **Description**:
  - 修改PostDetailView.vue中的评论渲染逻辑
  - 顶级评论按照原来的逻辑渲染
  - 顶级评论下的所有回复（无论层级）显示为同一级
  - 当评论的父ID和所属顶级评论ID不一致时，在用户名后面显示回复的用户名
- **Acceptance Criteria Addressed**: AC-3, AC-4
- **Test Requirements**:
  - `human-judgment` TR-4.1: 检查评论渲染是否避免了无限嵌套
  - `human-judgment` TR-4.2: 检查回复用户名是否正确显示
  - `programmatic` TR-4.3: 检查评论展开/折叠功能是否正常工作
- **Notes**: 参考抖音评论区的显示效果，确保评论显示清晰

## [ ] 任务 5: 测试评论系统功能
- **Priority**: P1
- **Depends On**: 任务 1, 任务 2, 任务 3, 任务 4
- **Description**:
  - 测试评论的发布、删除、回复功能
  - 测试评论的查询和渲染功能
  - 测试评论的分页功能
  - 测试评论的显示效果
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-3, AC-4
- **Test Requirements**:
  - `programmatic` TR-5.1: 测试评论发布功能是否正常
  - `programmatic` TR-5.2: 测试评论删除功能是否正常
  - `programmatic` TR-5.3: 测试评论回复功能是否正常
  - `programmatic` TR-5.4: 测试评论查询功能是否正常
  - `human-judgment` TR-5.5: 测试评论显示效果是否清晰
- **Notes**: 确保所有评论功能正常运行，用户体验良好