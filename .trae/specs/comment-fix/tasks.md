# 评论功能修复 - 实现计划

## [x] Task 1: 分析前端评论展示问题
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 分析前端PostDetailView.vue和comment.ts中的评论加载逻辑
  - 找出评论列表不展示的具体原因
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `human-judgement` TR-1.1: 确认评论加载逻辑是否正确
  - `programmatic` TR-1.2: 检查控制台是否有错误信息
- **Notes**: 重点检查fetchComments函数和commentStore中的相关方法

## [x] Task 2: 修复前端评论展示逻辑
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 根据分析结果修复前端评论加载逻辑
  - 确保使用分页查询接口获取评论
  - 确保评论列表正确渲染
- **Acceptance Criteria Addressed**: AC-1, AC-2
- **Test Requirements**:
  - `human-judgement` TR-2.1: 验证评论列表是否正常展示
  - `human-judgement` TR-2.2: 验证分页加载功能是否正常
  - `programmatic` TR-2.3: 检查网络请求是否正确调用分页接口
- **Notes**: 确保在fetchComments函数中正确处理分页逻辑

## [x] Task 3: 删除后端未使用的getCommentsByPostId接口
- **Priority**: P1
- **Depends On**: None
- **Description**: 
  - 在CommentController.java中删除getCommentsByPostId方法
  - 确保相关的Service和Mapper方法也被删除（如果未被其他地方使用）
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 确认getCommentsByPostId接口已不存在
  - `programmatic` TR-3.2: 验证其他评论接口仍然正常工作
- **Notes**: 检查是否有其他地方调用了这个接口

## [x] Task 4: 测试评论功能
- **Priority**: P1
- **Depends On**: Task 2, Task 3
- **Description**: 
  - 测试帖子详情页的评论列表展示
  - 测试分页加载功能
  - 测试评论发布和删除功能
  - 测试性能（模拟大量评论）
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-4
- **Test Requirements**:
  - `human-judgement` TR-4.1: 验证评论列表是否正常展示
  - `human-judgement` TR-4.2: 验证分页加载功能正常
  - `programmatic` TR-4.3: 验证性能测试通过
  - `programmatic` TR-4.4: 验证所有评论相关接口正常工作
- **Notes**: 可以使用浏览器开发者工具模拟网络请求和查看控制台错误