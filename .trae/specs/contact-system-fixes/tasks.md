# 联系人系统问题修复 - 实现计划

## [/] Task 1: 修复联系人列表红点标记功能
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 检查并修复联系人列表的红点标记功能
  - 确保未读消息数正确显示在联系人卡片上
  - 验证消息已读后红点标记消失
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 发送新消息后，对应联系人卡片显示红点标记
  - `programmatic` TR-1.2: 查看消息后，红点标记消失
- **Notes**: 检查messageStore中的unreadCount逻辑和MessageListView.vue中的渲染逻辑

## [ ] Task 2: 解决普通用户无法发送文件的问题
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 检查普通用户发送文件的权限配置
  - 修复文件上传功能，确保普通用户可以发送文件
  - 验证文件上传和发送流程正常
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-2.1: 普通用户登录后可以上传文件
  - `programmatic` TR-2.2: 文件上传成功并发送给接收方
- **Notes**: 检查FileController的权限配置和前端的文件上传逻辑

## [x] Task 3: 修复通讯录页面报错问题
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 修复AddFriendView.vue中的trim()方法调用错误
  - 确保通讯录页面正常加载，显示好友列表
  - 验证页面功能正常
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 通讯录页面加载无报错
  - `programmatic` TR-3.2: 好友列表正常显示
- **Notes**: 检查AddFriendView.vue第15行的trim()方法调用

## [ ] Task 4: 为通讯录按钮添加不同图标
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 为通讯录按钮添加与消息按钮不同的图标
  - 确保图标显示正常，易于区分
  - 保持界面风格一致
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `human-judgment` TR-4.1: 通讯录按钮使用与消息按钮不同的图标
  - `human-judgment` TR-4.2: 图标显示清晰，易于识别
- **Notes**: 可以使用现有图标或添加新图标

## [ ] Task 5: 优化移动端布局
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 优化移动端布局，解决右侧留白过大的问题
  - 确保页面在不同屏幕尺寸上都有良好的显示效果
  - 保持布局的响应式设计
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `human-judgment` TR-5.1: 移动端页面左右留白平衡
  - `human-judgment` TR-5.2: 布局在不同屏幕尺寸上显示正常
- **Notes**: 检查App.vue和相关组件的CSS样式