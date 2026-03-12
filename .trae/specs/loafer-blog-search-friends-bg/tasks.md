# 技术博客系统增强 - 实现计划

## [ ] 任务 1：修改 init.sql 脚本，为 post 表添加 ts_vector_col 字段
- **优先级**：P0
- **依赖**：None
- **描述**：
  - 修改 init.sql 文件，在 post 表中添加 ts_vector_col tsvector 字段
  - 使用 english 分词器，设置为 GENERATED ALWAYS AS 存储列
- **验收标准**：AC-4
- **测试要求**：
  - `programmatic` TR-1.1：执行 init.sql 脚本后，post 表应包含 ts_vector_col 字段
  - `programmatic` TR-1.2：验证 ts_vector_col 字段使用 english 分词器
- **备注**：确保字段定义正确，遵循 PostgreSQL 语法

## [ ] 任务 2：创建 init_jieba.sql 脚本文件
- **优先级**：P0
- **依赖**：任务 1
- **描述**：
  - 创建 init_jieba.sql 文件，重写 user 表 DDL
  - 在 post 表中添加 ts_vector_col tsvector 字段，使用 jiebacfg 分词器
- **验收标准**：AC-5
- **测试要求**：
  - `programmatic` TR-2.1：执行 init_jieba.sql 脚本后，post 表应包含 ts_vector_col 字段
  - `programmatic` TR-2.2：验证 ts_vector_col 字段使用 jiebacfg 分词器
- **备注**：确保脚本包含完整的数据库初始化逻辑

## [ ] 任务 3：修改 PostController，添加搜索分页功能
- **优先级**：P0
- **依赖**：任务 1, 任务 2
- **描述**：
  - 修改 searchPosts 方法，添加 page 和 size 参数
  - 更新 API 接口，支持分页查询
- **验收标准**：AC-1
- **测试要求**：
  - `programmatic` TR-3.1：验证搜索接口支持 page 和 size 参数
  - `programmatic` TR-3.2：验证返回结果包含分页信息
- **备注**：确保向后兼容，无参数时使用默认值

## [ ] 任务 4：修改 PostService，实现两套搜索方案
- **优先级**：P0
- **依赖**：任务 3
- **描述**：
  - 实现 jieba 分词器检测逻辑
  - 实现方案 A（传统 like 查询）和方案 B（jieba 全文检索）
  - 实现搜索结果高亮显示
- **验收标准**：AC-2, AC-3
- **测试要求**：
  - `programmatic` TR-4.1：验证未安装 jieba 时使用方案 A
  - `programmatic` TR-4.2：验证已安装 jieba 时使用方案 B
  - `programmatic` TR-4.3：验证搜索结果包含高亮显示
- **备注**：使用 try-catch 捕获 jieba 相关错误，确保系统稳定性

## [ ] 任务 5：实现添加好友功能前端界面
- **优先级**：P1
- **依赖**：None
- **描述**：
  - 开发添加好友页面组件
  - 实现好友搜索和添加功能
  - 集成后端添加好友 API
- **验收标准**：AC-6
- **测试要求**：
  - `human-judgment` TR-5.1：验证添加好友页面 UI 美观
  - `programmatic` TR-5.2：验证添加好友功能正常工作
- **备注**：确保表单验证和错误处理

## [ ] 任务 6：优化全局背景视觉效果
- **优先级**：P2
- **依赖**：None
- **描述**：
  - 设计并实现炫酷的 CSS 动画效果
  - 确保动画流畅，不影响页面性能
- **验收标准**：AC-7
- **测试要求**：
  - `human-judgment` TR-6.1：验证背景动画效果炫酷
  - `programmatic` TR-6.2：验证动画不影响页面性能
- **备注**：使用 CSS3 动画或 Canvas 实现，确保浏览器兼容性

## [ ] 任务 7：测试和验证
- **优先级**：P0
- **依赖**：任务 1-6
- **描述**：
  - 测试所有功能是否正常工作
  - 验证搜索功能在不同环境下的表现
  - 测试添加好友功能
  - 验证背景动画效果
- **验收标准**：所有 AC
- **测试要求**：
  - `programmatic` TR-7.1：验证所有功能正常工作
  - `human-judgment` TR-7.2：验证用户体验良好
- **备注**：编写测试用例，确保功能稳定性