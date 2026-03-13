# 评论功能修复 - 实现计划

## [ ] Task 1: 修复评论时间显示异常问题
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 分析 formatDate 函数的问题
  - 修复日期解析和格式化逻辑
  - 确保时间显示正确，不出现 NaN
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `human-judgement` TR-1.1: 验证评论时间显示格式正确，不出现 NaN
- **Notes**: 重点检查 formatDate 函数的实现

## [ ] Task 2: 确保评论加载时获取回复数
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 检查后端API是否返回评论的回复数
  - 如果没有，修改前端逻辑，在评论加载时查询回复数
  - 确保有回复的评论显示回复数和展开按钮
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `human-judgement` TR-2.1: 验证有回复的评论显示回复数和展开按钮
- **Notes**: 可能需要修改 commentStore 的实现

## [ ] Task 3: 优化展开/折叠按钮样式
- **Priority**: P1
- **Depends On**: Task 2
- **Description**: 
  - 优化展开/折叠按钮的样式
  - 确保按钮样式与整体UI风格一致
  - 添加适当的动画效果
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `human-judgement` TR-3.1: 验证展开/折叠按钮样式美观
- **Notes**: 可以参考其他按钮的样式设计

## [ ] Task 4: 确保回复加载和渲染正常
- **Priority**: P0
- **Depends On**: Task 2
- **Description**: 
  - 测试展开/折叠功能
  - 确保点击展开按钮后能正确加载和渲染回复
  - 确保回复的时间显示也正确
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `human-judgement` TR-4.1: 验证点击展开/折叠按钮功能正常
  - `human-judgement` TR-4.2: 验证回复的时间显示正确
- **Notes**: 测试不同场景下的回复加载

## [ ] Task 5: 整体测试和优化
- **Priority**: P1
- **Depends On**: Task 1, Task 2, Task 3, Task 4
- **Description**: 
  - 测试所有修复的功能
  - 确保评论功能整体正常工作
  - 优化用户体验
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-3, AC-4
- **Test Requirements**:
  - `human-judgement` TR-5.1: 验证所有功能正常工作
  - `human-judgement` TR-5.2: 验证整体UI效果美观
- **Notes**: 在不同设备和浏览器上测试