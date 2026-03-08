# Loafer Blog 功能修复 - 实现计划 (v4)

## [x] Task 1: 修复管理员帖子管理功能入口
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 检查AdminView.vue中的导航栏配置
  - 确保帖子管理选项卡正确显示
  - 验证管理员可以访问帖子管理功能
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 管理员登录后进入后台可以看到帖子管理选项
  - `programmatic` TR-1.2: 点击帖子管理选项可以正常进入管理页面
- **Notes**: 检查路由配置和权限控制

## [x] Task 2: 修复注销账号后仍可登录的问题
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 检查后端注销账号的实现逻辑
  - 确保账号注销后在数据库中被标记为禁用
  - 修复登录验证逻辑，检查账号状态
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-2.1: 注销账号后数据库中账号状态被正确更新
  - `programmatic` TR-2.2: 注销后使用相同账号登录失败
- **Notes**: 检查用户表结构，确认是否有状态字段

## [x] Task 3: 实现帖子详情页的点赞和收藏按钮
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 在PostDetailView.vue中添加点赞和收藏按钮
  - 实现点赞和收藏的前端逻辑
  - 连接后端API实现功能
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 帖子详情页显示点赞和收藏按钮
  - `programmatic` TR-3.2: 点击按钮可以成功执行相应操作
- **Notes**: 检查后端是否已实现相应API

## [x] Task 4: 实现浏览历史功能
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 实现帖子浏览时记录历史的功能
  - 修复UserView.vue中浏览历史标签的数据获取
  - 确保浏览历史数据正确存储和显示
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-4.1: 浏览帖子后历史记录被添加
  - `programmatic` TR-4.2: 用户详情页的浏览历史标签显示正确数据
- **Notes**: 检查后端是否已实现浏览历史的API
