# LoaferBlog - 技术博客项目 - 实现计划

## [x] 任务 1: 后端项目初始化与基础配置
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 创建Spring Boot项目，配置Java 21
  - 集成MyBatis Plus和PostgreSQL
  - 配置基础依赖和项目结构
- **Acceptance Criteria Addressed**: AC-1, AC-2
- **Test Requirements**:
  - `programmatic` TR-1.1: 项目能正常启动，数据库连接成功
  - `human-judgement` TR-1.2: 项目结构清晰，依赖配置合理
- **Notes**: 使用Spring Initializr快速创建项目，配置必要的依赖项

## [x] 任务 2: 数据库设计与初始化
- **Priority**: P0
- **Depends On**: 任务 1
- **Description**: 
  - 设计数据库表结构（用户、权限、公告、帖子、评论、消息、分类、标签、敏感词等）
  - 创建数据库初始化脚本
  - 实现实体类和Mapper接口
- **Acceptance Criteria Addressed**: AC-1, AC-3, AC-4, AC-5, AC-6, AC-7, AC-8, AC-9
- **Test Requirements**:
  - `programmatic` TR-2.1: 数据库表结构正确创建
  - `programmatic` TR-2.2: 实体类与数据库表映射正确
- **Notes**: 考虑表之间的关联关系，使用MyBatis Plus的代码生成器

## [x] 任务 3: 后端API实现 - 用户认证
- **Priority**: P0
- **Depends On**: 任务 2
- **Description**: 
  - 实现用户注册、登录、注销接口
  - 实现JWT token生成和验证
  - 实现密码加密和验证
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-3.1: 注册接口返回200状态码
  - `programmatic` TR-3.2: 登录接口返回JWT token
  - `programmatic` TR-3.3: 注销接口正常工作
- **Notes**: 使用Spring Security和JWT实现认证功能

## [x] 任务 4: 后端API实现 - 权限管理
- **Priority**: P0
- **Depends On**: 任务 3
- **Description**: 
  - 实现权限控制中间件
  - 实现角色管理接口
  - 实现权限验证逻辑
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-4.1: 普通用户无法访问管理员接口
  - `programmatic` TR-4.2: 管理员可以访问所有接口
- **Notes**: 使用Spring Security的权限控制功能

## [x] 任务 5: 后端API实现 - 公告管理
- **Priority**: P1
- **Depends On**: 任务 4
- **Description**: 
  - 实现公告的CRUD接口
  - 实现公告列表查询接口
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-5.1: 管理员可以发布公告
  - `programmatic` TR-5.2: 所有用户可以查看公告列表
- **Notes**: 公告按发布时间倒序排列

## [x] 任务 6: 后端API实现 - 技术贴管理
- **Priority**: P1
- **Depends On**: 任务 4
- **Description**: 
  - 实现技术贴的CRUD接口
  - 实现技术贴搜索接口
  - 实现技术贴列表查询接口
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-6.1: 管理员可以发布技术贴
  - `programmatic` TR-6.2: 所有用户可以搜索技术贴
  - `programmatic` TR-6.3: 管理员可以编辑删除技术贴
  - `programmatic` TR-6.4: 普通用户无法发布技术贴
- **Notes**: 搜索功能使用PostgreSQL的全文搜索

## [x] 任务 7: 后端API实现 - 评论系统
- **Priority**: P1
- **Depends On**: 任务 6
- **Description**: 
  - 实现评论的CRUD接口
  - 实现评论回复功能
  - 实现评论列表查询接口
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-7.1: 登录用户可以评论帖子
  - `programmatic` TR-7.2: 登录用户可以回复评论
  - `programmatic` TR-7.3: 管理员可以删除评论
- **Notes**: 评论按时间顺序排列

## [x] 任务 8: 后端API实现 - 消息系统
- **Priority**: P1
- **Depends On**: 任务 4
- **Description**: 
  - 实现消息的发送和接收接口
  - 实现消息列表查询接口
  - 实现消息回复功能
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `programmatic` TR-8.1: 普通用户可以发送消息给管理员
  - `programmatic` TR-8.2: 管理员可以查看和回复消息
- **Notes**: 消息按时间顺序排列

## [x] 任务 9: 后端API实现 - 分类管理
- **Priority**: P1
- **Depends On**: 任务 4
- **Description**: 
  - 实现分类的CRUD接口
  - 实现分类与帖子的关联管理
  - 实现删除分类时的关联处理
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-9.1: 管理员可以新增、编辑、删除分类
  - `programmatic` TR-9.2: 删除分类时自动移除帖子的分类信息
- **Notes**: 使用多对多关系管理分类和帖子

## [x] 任务 10: 后端API实现 - 标签管理
- **Priority**: P1
- **Depends On**: 任务 4
- **Description**: 
  - 实现标签的CRUD接口
  - 实现标签与帖子的关联管理
  - 实现删除标签时的关联处理
- **Acceptance Criteria Addressed**: AC-8
- **Test Requirements**:
  - `programmatic` TR-10.1: 管理员可以新增、编辑、删除标签
  - `programmatic` TR-10.2: 删除标签时自动移除帖子的标签信息
- **Notes**: 使用多对多关系管理标签和帖子

## [x] 任务 11: 后端API实现 - 敏感词过滤
- **Priority**: P1
- **Depends On**: 任务 4
- **Description**: 
  - 实现敏感词库管理接口
  - 实现敏感词过滤功能
  - 实现评论和消息的敏感词处理
- **Acceptance Criteria Addressed**: AC-9
- **Test Requirements**:
  - `programmatic` TR-11.1: 包含敏感词的评论能正确过滤
  - `programmatic` TR-11.2: 数据库存储原文和过滤后结果
- **Notes**: 使用高效的敏感词匹配算法

## [x] 任务 12: 前端项目初始化与基础配置
- **Priority**: P0
- **Depends On**: None
- **Description**: 
  - 创建Vue3 + TypeScript + Vite项目
  - 配置基础依赖和项目结构
  - 集成Vue Router和Pinia
- **Acceptance Criteria Addressed**: AC-1, AC-2, AC-3, AC-4, AC-5, AC-6, AC-7, AC-8, AC-9
- **Test Requirements**:
  - `programmatic` TR-12.1: 项目能正常构建
  - `human-judgement` TR-12.2: 项目结构清晰，依赖配置合理
- **Notes**: 使用Vite创建项目，配置必要的依赖项

## [x] 任务 13: 前端实现 - 用户认证
- **Priority**: P0
- **Depends On**: 任务 3, 任务 12
- **Description**: 
  - 实现登录、注册、注销页面
  - 实现JWT token管理
  - 实现用户状态管理
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `programmatic` TR-13.1: 登录功能正常工作
  - `programmatic` TR-13.2: 注册功能正常工作
  - `programmatic` TR-13.3: 注销功能正常工作
- **Notes**: 使用Pinia管理用户状态

## [x] 任务 14: 前端实现 - 权限管理
- **Priority**: P0
- **Depends On**: 任务 4, 任务 13
- **Description**: 
  - 实现权限控制路由
  - 实现权限控制组件
  - 实现权限验证逻辑
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-14.1: 普通用户无法访问管理员页面
  - `programmatic` TR-14.2: 管理员可以访问所有页面
- **Notes**: 使用Vue Router的导航守卫实现权限控制

## [x] 任务 15: 前端实现 - 公告管理
- **Priority**: P1
- **Depends On**: 任务 5, 任务 14
- **Description**: 
  - 实现公告列表页面
  - 实现公告详情页面
  - 实现管理员公告管理页面
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-15.1: 所有用户可以查看公告列表
  - `programmatic` TR-15.2: 管理员可以发布、编辑、删除公告
- **Notes**: 公告按发布时间倒序排列

## [x] 任务 16: 前端实现 - 技术贴管理
- **Priority**: P1
- **Depends On**: 任务 6, 任务 14
- **Description**: 
  - 实现技术贴列表页面
  - 实现技术贴详情页面
  - 实现技术贴发布和编辑页面（仅管理员可见）
  - 实现技术贴搜索功能
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-16.1: 所有用户可以搜索技术贴
  - `programmatic` TR-16.2: 管理员可以发布技术贴
  - `programmatic` TR-16.3: 管理员可以编辑删除技术贴
  - `programmatic` TR-16.4: 普通用户无法看到发布和编辑按钮
- **Notes**: 搜索功能支持关键词高亮

## [x] 任务 17: 前端实现 - 评论系统
- **Priority**: P1
- **Depends On**: 任务 7, 任务 14
- **Description**: 
  - 实现评论列表组件
  - 实现评论输入组件
  - 实现评论回复功能
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-17.1: 登录用户可以评论帖子
  - `programmatic` TR-17.2: 登录用户可以回复评论
  - `programmatic` TR-17.3: 管理员可以删除评论
- **Notes**: 评论按时间顺序排列

## [x] 任务 18: 前端实现 - 消息系统
- **Priority**: P1
- **Depends On**: 任务 8, 任务 14
- **Description**: 
  - 实现消息列表页面
  - 实现消息发送页面
  - 实现消息回复功能
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `programmatic` TR-18.1: 普通用户可以发送消息给管理员
  - `programmatic` TR-18.2: 管理员可以查看和回复消息
- **Notes**: 消息按时间顺序排列

## [x] 任务 19: 前端实现 - 分类管理
- **Priority**: P1
- **Depends On**: 任务 9, 任务 14
- **Description**: 
  - 实现分类列表组件
  - 实现分类管理页面
  - 实现分类选择组件
- **Acceptance Criteria Addressed**: AC-7
- **Test Requirements**:
  - `programmatic` TR-19.1: 管理员可以新增、编辑、删除分类
  - `programmatic` TR-19.2: 帖子可以选择多个分类
- **Notes**: 分类管理页面仅对管理员开放

## [x] 任务 20: 前端实现 - 标签管理
- **Priority**: P1
- **Depends On**: 任务 10, 任务 14
- **Description**: 
  - 实现标签列表组件
  - 实现标签管理页面
  - 实现标签选择组件
- **Acceptance Criteria Addressed**: AC-8
- **Test Requirements**:
  - `programmatic` TR-20.1: 管理员可以新增、编辑、删除标签
  - `programmatic` TR-20.2: 帖子可以选择多个标签
- **Notes**: 标签管理页面仅对管理员开放

## [x] 任务 21: 前端实现 - 敏感词过滤
- **Priority**: P1
- **Depends On**: 任务 11, 任务 14
- **Description**:
  - 实现评论和消息输入组件
  - 实现敏感词过滤结果展示
- **Acceptance Criteria Addressed**: AC-9
- **Test Requirements**:
  - `programmatic` TR-21.1: 包含敏感词的评论能正确显示过滤后结果
- **Notes**: 敏感词过滤在前端和后端都要实现，前端作为辅助过滤

## [x] 任务 22: 系统集成与测试
- **Priority**: P0
- **Depends On**: 所有任务
- **Description**: 
  - 整合前端和后端代码
  - 进行系统测试
  - 修复集成过程中的问题
- **Acceptance Criteria Addressed**: 所有AC
- **Test Requirements**:
  - `programmatic` TR-22.1: 所有API接口正常工作
  - `programmatic` TR-22.2: 前端页面正常显示和交互
  - `human-judgement` TR-22.3: 系统整体功能完整
- **Notes**: 进行端到端测试，确保系统各模块协同工作