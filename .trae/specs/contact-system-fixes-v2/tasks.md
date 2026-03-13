# 联系人系统修复 - 实现计划

## [x] Task 1: 修复联系人列表最近一条消息不展示的问题
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 检查MessageListView.vue中的消息显示逻辑
  - 修复最近一条消息不展示的问题
  - 确保文本消息和图片消息都能正常显示
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `human-judgment` TR-1.1: 最近一条消息正常显示
  - `human-judgment` TR-1.2: 文本消息和图片消息都能显示

## [x] Task 2: 修复红点显示不全的问题
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 检查MessageListView.vue中的红点样式
  - 修复红点被截断的问题
  - 确保红点完整显示在头像右上角
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `human-judgment` TR-2.1: 红点完整显示，不被截断
  - `human-judgment` TR-2.2: 红点位置正确

## [x] Task 3: 优化布局美观度
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 检查并优化App.vue和相关组件的布局
  - 确保左右留白平衡
  - 恢复并提升布局美观度
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `human-judgment` TR-3.1: 布局美观，左右留白平衡
  - `human-judgment` TR-3.2: 导航栏显示正常
