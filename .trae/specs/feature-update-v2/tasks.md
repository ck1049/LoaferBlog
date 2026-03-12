# LoaferBlog 功能更新 v2 - 实现计划

## [ ] 任务 1: 聊天消息文件下载功能实现
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 确保文件消息的链接正确指向文件存储路径
  - 实现文件下载功能，支持各种文件类型
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 点击文件链接后，文件开始下载
  - `programmatic` TR-1.2: 下载的文件内容与上传的文件一致

## [ ] 任务 2: 聊天消息图片视频预览和保存功能
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 实现图片点击预览功能，支持放大查看
  - 实现视频在线播放功能
  - 添加保存图片和视频的选项
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-2.1: 点击图片后显示预览窗口
  - `programmatic` TR-2.2: 点击视频后开始在线播放
  - `programmatic` TR-2.3: 预览窗口提供保存按钮，点击后可保存文件

## [ ] 任务 3: 聊天消息滚动逻辑优化
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 进入聊天窗口时，自动滚动到最底部显示最新消息
  - 发送消息时，自动滚动到最底部
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 进入聊天窗口后，滚动条位于最底部
  - `programmatic` TR-3.2: 发送消息后，滚动条自动滚动到最底部

## [ ] 任务 4: 添加好友功能前端实现
- **Priority**: P1
- **Depends On**: 任务 5
- **Description**:
  - 创建添加好友页面
  - 实现用户名搜索功能
  - 实现分页加载和"加载更多"按钮
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-4.1: 输入用户名后点击搜索，显示匹配的用户列表
  - `programmatic` TR-4.2: 滚动到页面底部时显示"加载更多"按钮
  - `programmatic` TR-4.3: 点击"加载更多"按钮后加载下一页数据

## [ ] 任务 5: 添加好友功能后端实现
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 实现用户搜索接口，支持按用户名模糊匹配
  - 实现分页查询，按照用户 ID 排序
  - 添加用户 ID 大于参数的查询条件，避免深度分页问题
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-5.1: 调用搜索接口返回正确的用户列表
  - `programmatic` TR-5.2: 传入 lastId 参数后，返回 ID 大于该值的用户
  - `programmatic` TR-5.3: 结果按照用户 ID 排序

## [ ] 任务 6: 全局背景视觉效果优化
- **Priority**: P2
- **Depends On**: None
- **Description**:
  - 设计并实现炫酷的 CSS 背景动画
  - 确保动画效果不影响页面性能
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `human-judgment` TR-6.1: 页面背景显示炫酷的动画效果
  - `programmatic` TR-6.2: 页面加载速度不受动画影响

## [ ] 任务 7: 帖子收藏数统计修复
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 排查收藏数归零的原因
  - 修复收藏数统计逻辑，确保数据持久化
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `programmatic` TR-7.1: 收藏帖子后刷新页面，收藏数保持不变
  - `programmatic` TR-7.2: 多次刷新页面，收藏数不会归零

## [ ] 任务 8: 帖子浏览次数统计修复
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 排查浏览次数加 2 的原因
  - 修复浏览次数统计逻辑，确保每次刷新只增加 1 次
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-8.1: 访问帖子详情页并刷新，浏览次数只增加 1 次
  - `programmatic` TR-8.2: 多次刷新页面，浏览次数每次只增加 1 次