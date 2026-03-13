# 评论功能优化 - 实现计划

## [ ] Task 1: 修复循环嵌套数据库查询问题
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 修改CommentServiceImpl中的getCommentsByPostIdWithPagination方法，改为批量查询用户信息
  - 修改getRepliesByCommentId方法，同样改为批量查询用户信息
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-1.1: 验证数据库查询次数减少
  - `programmatic` TR-1.2: 验证评论列表加载性能提升
- **Notes**: 使用Collects工具类提取所有userId，然后使用userMapper.selectBatchIds方法批量查询

## [ ] Task 2: 替换User对象为UserVO
- **Priority**: P0
- **Depends On**: Task 1
- **Description**: 
  - 修改Comment实体类，将user字段类型从User改为UserVO
  - 修改CommentServiceImpl，在批量查询用户信息后转换为UserVO
  - 修改CommentController，确保返回的是包含UserVO的Comment对象
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-2.1: 验证接口返回的是UserVO而非User对象
  - `programmatic` TR-2.2: 验证邮箱已脱敏显示
- **Notes**: 确保所有返回用户信息的地方都使用UserVO

## [ ] Task 3: 整理项目结构
- **Priority**: P1
- **Depends On**: None
- **Description**: 
  - 删除与model同级的dto目录
  - 删除与model同级的vo目录
  - 确保所有dto和vo都在model目录下
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `human-judgment` TR-3.1: 验证项目结构符合规范
  - `human-judgment` TR-3.2: 验证没有重复的UserVO
- **Notes**: 确保删除前备份必要的文件

## [ ] Task 4: 测试评论功能
- **Priority**: P1
- **Depends On**: Task 1, Task 2, Task 3
- **Description**: 
  - 测试评论列表加载性能
  - 测试评论接口返回的数据结构
  - 测试用户信息的安全性
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-3
- **Test Requirements**:
  - `programmatic` TR-4.1: 验证性能测试通过
  - `programmatic` TR-4.2: 验证安全测试通过
  - `human-judgment` TR-4.3: 验证项目结构符合规范
- **Notes**: 使用浏览器开发者工具查看网络请求和响应