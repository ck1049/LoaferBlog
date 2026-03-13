# 联系人系统升级 - 实现计划

## [x] Task 1: 修复联系人卡片红点标记功能
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 检查并修复联系人卡片的未读消息红点标记功能
  - 确保未读消息数正确显示在联系人卡片上
  - 验证消息已读后红点标记消失
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 发送新消息后，对应联系人卡片显示红点标记
  - `programmatic` TR-1.2: 查看消息后，红点标记消失
- **Notes**: 检查messageStore中的unreadCount逻辑和MessageListView.vue中的渲染逻辑

## [x] Task 2: 修复普通用户文件上传权限问题
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 检查并修复普通用户发送消息时无法上传文件的问题
  - 确保普通用户和管理员具有相同的文件上传权限
  - 验证文件上传功能正常工作
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-2.1: 普通用户登录后可以看到文件上传按钮
  - `programmatic` TR-2.2: 普通用户可以成功上传文件并发送
- **Notes**: 检查用户角色权限判断逻辑，特别是文件上传相关的权限检查

## [x] Task 3: 修复通讯录页面trim()错误
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 修复AddFriendView.vue中的trim()错误：Cannot read properties of undefined (reading 'trim')
  - 确保通讯录页面正常显示，不再报错
  - 验证页面功能正常
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-3.1: 通讯录页面加载时无trim()错误
  - `programmatic` TR-3.2: 通讯录页面正常显示好友列表
- **Notes**: 检查AddFriendView.vue中第15行附近的代码，确保在调用trim()前检查变量是否为undefined

## [x] Task 4: 区分通讯录图标和消息图标
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 确保通讯录图标与消息图标不同，避免混淆
  - 选择合适的图标区分两个功能
  - 验证图标显示正确
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `human-judgment` TR-4.1: 通讯录图标与消息图标不同
  - `human-judgment` TR-4.2: 图标清晰易辨
- **Notes**: 检查App.vue中的图标配置，确保使用不同的图标

## [x] Task 5: 修复移动端布局问题
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 修复移动端布局问题，调整左右留白使其平衡
  - 确保在不同尺寸的移动设备上布局正常
  - 验证布局美观且易用
- **Acceptance Criteria Addressed**: AC-8
- **Test Requirements**:
  - `human-judgment` TR-5.1: 移动端页面左右留白平衡
  - `human-judgment` TR-5.2: 布局在不同尺寸设备上显示正常
- **Notes**: 检查CSS样式文件中的移动端布局设置，调整padding和margin

## [ ] Task 6: 优化联系人列表卡片显示最新消息
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 修改联系人列表卡片，默认展示最新一条消息
  - 根据消息类型显示不同内容：文本消息显示内容，图片/视频/文件消息显示类型标识和文件名
  - 对消息内容进行适当截断
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `human-judgment` TR-6.1: 联系人卡片显示最新消息内容
  - `human-judgment` TR-6.2: 图片/视频/文件消息显示正确的类型标识和文件名
- **Notes**: 检查MessageListView.vue中的消息展示逻辑

## [ ] Task 7: 实现点击好友卡片跳转聊天窗口
- **Priority**: P1
- **Depends On**: Task 3
- **Description**:
  - 在通讯录页面中，为好友列表卡片添加点击事件
  - 点击好友卡片时，自动跳转到与该好友的聊天窗口页面
  - 确保跳转后聊天窗口功能正常
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-7.1: 点击好友卡片正确跳转到聊天窗口页面
  - `programmatic` TR-7.2: 跳转后聊天窗口显示正确的聊天记录
- **Notes**: 检查路由跳转逻辑和聊天窗口的初始化逻辑

## [ ] Task 8: 实现搜索好友状态判断功能
- **Priority**: P2
- **Depends On**: Task 3
- **Description**:
  - 在通讯录页面的搜索功能中，判断搜索结果是否已为好友
  - 对于已添加的好友，显示"发消息"按钮
  - 对于未添加的用户，显示"添加好友"按钮
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `programmatic` TR-8.1: 搜索已添加的好友，显示"发消息"按钮
  - `programmatic` TR-8.2: 搜索未添加的用户，显示"添加好友"按钮
- **Notes**: 检查搜索逻辑和好友关系判断逻辑