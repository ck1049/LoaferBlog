# 联系人系统增强 - 实现计划

## [x] Task 1: 修复联系人列表消息显示问题
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 修改MessageListView.vue中的消息显示逻辑
  - 图片消息显示文件名而不是"[图片]"
  - 文本消息正常显示内容
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `human-judgment` TR-1.1: 图片消息显示"[文件名]"
  - `human-judgment` TR-1.2: 文本消息显示实际内容

## [x] Task 2: 调整红点位置到头像右上角
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 修改MessageListView.vue中的红点样式和位置
  - 将红点从时间旁边移动到头像右上角
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `human-judgment` TR-2.1: 红点显示在头像右上角
  - `human-judgment` TR-2.2: 红点位置在不同设备上保持一致

## [x] Task 3: 实现好友请求确认功能
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 前端添加好友请求通知组件
  - 后端实现好友请求状态管理
  - 用户登录时显示未处理的好友请求
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 用户A发送好友请求后，用户B登录能看到请求
  - `programmatic` TR-3.2: 用户B能成功确认或拒绝好友请求

## [x] Task 4: 修复移动端布局左右留白平衡
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 修改App.vue和相关组件的移动端样式
  - 确保左右留白平衡
  - 适配不同分辨率的移动设备
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `human-judgment` TR-4.1: 移动端左右留白平衡
  - `human-judgment` TR-4.2: 在不同分辨率设备上显示正常

## [x] Task 5: 修复管理页面导航栏越界问题
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 修改管理页面的导航栏样式
  - 确保导航栏在移动端不越界
  - 优化导航栏在不同屏幕尺寸下的显示
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `human-judgment` TR-5.1: 管理页面导航栏不越界
  - `human-judgment` TR-5.2: 导航栏在移动端显示正常