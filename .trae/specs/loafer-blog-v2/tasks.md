# Loafer Blog 二次元风格改造与功能增强 - 实现计划

## [x] 任务 1: UI风格改造为二次元风格
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 设计并实现二次元风格的UI，包括色彩方案、图标、布局等
  - 更新前端组件和页面样式
  - 确保响应式设计，适配不同设备
- **Acceptance Criteria Addressed**: AC-1
- **Test Requirements**:
  - `human-judgement` TR-1.1: 页面呈现二次元风格，色彩鲜艳，图标可爱，布局现代
  - `human-judgement` TR-1.2: 视觉效果符合年轻群体审美，活力满满
- **Notes**: 可以参考流行的二次元网站设计，使用适合的色彩搭配和元素

## [x] 任务 2: 公告功能优化
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 修改公告默认展示方式，默认隐藏，点击图标显示
  - 实现公告已读未读状态管理
  - 在图标上添加未读公告红点标识
  - 后端添加公告状态管理接口
- **Acceptance Criteria Addressed**: AC-2
- **Test Requirements**:
  - `programmatic` TR-2.1: 未读公告时图标显示红点
  - `programmatic` TR-2.2: 点击图标显示公告列表
  - `programmatic` TR-2.3: 查看公告后状态更新为已读
- **Notes**: 需要在数据库中添加公告阅读状态字段

## [x] 任务 3: 前端支持Markdown渲染
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 引入Markdown渲染库（如marked.js或markdown-it）
  - 实现技术贴内容的Markdown渲染
  - 优化渲染效果，支持代码高亮等功能
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-3.1: 技术贴内容正确渲染为HTML
  - `programmatic` TR-3.2: 代码块正确高亮显示
  - `programmatic` TR-3.3: 表格、列表等元素正确渲染
- **Notes**: 选择轻量且功能完善的Markdown渲染库

## [x] 任务 4: 后端防注入处理
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 对技术贴内容进行防XSS处理
  - 确保数据库操作安全，防止SQL注入
  - 实现内容过滤和验证
- **Acceptance Criteria Addressed**: AC-3
- **Test Requirements**:
  - `programmatic` TR-4.1: 输入恶意脚本内容时被过滤
  - `programmatic` TR-4.2: 数据库操作参数化，防止SQL注入
- **Notes**: 使用现有的安全框架和工具

## [x] 任务 5: 管理员在线编辑发布功能
- **Priority**: P1
- **Depends On**: 任务 3
- **Description**:
  - 实现管理员在线编辑技术贴的界面
  - 支持富文本编辑和Markdown编辑模式
  - 实现发布、保存草稿等功能
- **Acceptance Criteria Addressed**: AC-4
- **Test Requirements**:
  - `programmatic` TR-5.1: 管理员可以在线编辑技术贴
  - `programmatic` TR-5.2: 支持实时预览编辑效果
  - `programmatic` TR-5.3: 发布功能正常工作
- **Notes**: 可以使用现有的富文本编辑器或Markdown编辑器

## [x] 任务 6: 媒体文件上传功能
- **Priority**: P1
- **Depends On**: 任务 5
- **Description**:
  - 实现文件上传接口
  - 支持图片、视频、音频文件上传
  - 生成可在Markdown中引用的文件地址
  - 实现文件存储和管理
- **Acceptance Criteria Addressed**: AC-4, AC-5
- **Test Requirements**:
  - `programmatic` TR-6.1: 上传文件成功并返回可引用地址
  - `programmatic` TR-6.2: 上传的媒体文件可以在技术贴中正确显示
  - `programmatic` TR-6.3: 文件上传大小和类型限制合理
- **Notes**: 需要配置文件存储路径和访问权限

## [x] 任务 7: Markdown文件上传支持
- **Priority**: P2
- **Depends On**: 任务 6
- **Description**:
  - 支持上传Markdown文件
  - 解析Markdown文件内容并显示在编辑器中
  - 处理文件中的图片引用
- **Acceptance Criteria Addressed**: AC-5
- **Test Requirements**:
  - `programmatic` TR-7.1: 上传Markdown文件成功并解析内容
  - `programmatic` TR-7.2: 文件中的图片引用正确处理
- **Notes**: 需要考虑文件编码和格式兼容性

## [x] 任务 8: 分类功能实现
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 实现分类管理功能（创建、编辑、删除分类）
  - 为技术贴添加分类关联
  - 实现按分类浏览和筛选功能
  - 后端添加分类相关接口
- **Acceptance Criteria Addressed**: AC-6
- **Test Requirements**:
  - `programmatic` TR-8.1: 分类管理功能正常工作
  - `programmatic` TR-8.2: 技术贴可以分配分类
  - `programmatic` TR-8.3: 可以按分类浏览技术贴
- **Notes**: 需要在数据库中添加分类表和关联表

## [x] 任务 9: 修复登录状态在页面刷新后丢失的bug
- **Priority**: P0
- **Depends On**: None
- **Description**:
  - 分析登录状态管理机制
  - 确保登录状态在页面刷新后保持
  - 检查localStorage或sessionStorage的使用
  - 修复前端状态管理逻辑
- **Acceptance Criteria Addressed**: 新增
- **Test Requirements**:
  - `programmatic` TR-9.1: 登录后刷新页面，用户信息仍然显示
  - `programmatic` TR-9.2: 登录状态持久化正常工作
- **Notes**: 可能需要检查前端状态管理库的配置，确保登录状态正确持久化

## [x] 任务 10: 改造后端使用DTO和VO替代Map接收/返回参数
- **Priority**: P1
- **Depends On**: None
- **Description**:
  - 创建DTO和VO类
  - 改造控制器和服务类，使用DTO和VO替代Map
  - 添加基于注解的参数校验
  - 更新相关实现代码
- **Acceptance Criteria Addressed**: 新增
- **Test Requirements**:
  - `programmatic` TR-10.1: 所有控制器不再使用Map接收参数
  - `programmatic` TR-10.2: 所有服务类不再使用Map返回数据
  - `programmatic` TR-10.3: 参数校验正常工作
- **Notes**: 需要为不同的业务场景创建相应的DTO和VO类