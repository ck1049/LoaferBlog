# 评论功能增强 - 实现计划

## [x] Task 1: 检查后端API是否返回用户头像信息
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 检查后端评论API是否返回用户头像信息
  - 如果没有，需要修改后端代码以返回用户信息
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 验证评论API返回的用户信息包含头像字段
- **Notes**: 重点检查CommentController和CommentService的实现

## [x] Task 2: 修改前端Comment接口类型定义
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 在comment.ts中添加用户头像字段到Comment接口
  - 确保类型定义与后端返回的数据结构一致
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-2.1: 验证Comment接口包含avatar字段
- **Notes**: 确保类型定义正确，避免类型错误

## [x] Task 3: 实现评论用户信息显示
- **Priority**: P0
- **Depends On**: Task 2
- **Description**: 
  - 在PostDetailView.vue中修改评论头部，添加用户头像显示
  - 确保头像和用户名正确显示
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `human-judgement` TR-3.1: 验证评论显示用户头像和用户名
- **Notes**: 注意头像的样式和大小

## [x] Task 4: 优化评论时间显示格式
- **Priority**: P1
- **Depends On**: None
- **Description**: 
  - 修改评论时间的显示格式，使其更加友好
  - 可以使用相对时间或更清晰的日期格式
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `human-judgement` TR-4.1: 验证评论时间显示格式美观清晰
- **Notes**: 可以使用现有的formatDate函数或引入第三方库

## [x] Task 5: 实现回复数显示和展开/折叠功能
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 添加评论回复数的显示
  - 实现展开/折叠按钮和功能
  - 默认只显示顶级评论，不自动加载回复
- **Acceptance Criteria Addressed**: AC-2, AC-3
- **Test Requirements**:
  - `human-judgement` TR-5.1: 验证评论显示回复数和展开按钮
  - `human-judgement` TR-5.2: 验证点击展开/折叠按钮功能正常
- **Notes**: 需要添加新的状态管理来处理展开/折叠状态

## [x] Task 6: 美化展开/折叠按钮样式
- **Priority**: P1
- **Depends On**: Task 5
- **Description**: 
  - 设计并实现美观的展开/折叠按钮样式
  - 确保按钮样式与整体UI风格一致
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `human-judgement` TR-6.1: 验证展开/折叠按钮样式美观
- **Notes**: 可以使用图标或文字按钮，确保响应式设计

## [x] Task 7: 测试评论功能
- **Priority**: P1
- **Depends On**: Task 3, Task 4, Task 5, Task 6
- **Description**: 
  - 测试评论用户信息显示
  - 测试回复数显示和展开/折叠功能
  - 测试按钮样式和整体UI效果
  - 测试响应式设计
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-3, AC-4
- **Test Requirements**:
  - `human-judgement` TR-7.1: 验证所有功能正常工作
  - `human-judgement` TR-7.2: 验证整体UI效果美观
- **Notes**: 在不同设备和浏览器上测试